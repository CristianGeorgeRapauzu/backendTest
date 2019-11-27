package io.golo.backendtest.service;

import io.golo.backendtest.model.AvailabilityDTO;
import io.golo.backendtest.model.PeekingDTO;
import io.golo.backendtest.service.utility.PeekWindow;
import io.golo.backendtest.service.utility.PeekingRegistrar;
import io.micrometer.core.instrument.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Service for status monitoring operations:
 * start, stop, status, ping
 */
@Service
public class MonitoringService {
    private static final String READY_STATE = "READY";

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringService.class);

	private RestTemplate restTemplate;
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private PeekingRegistrar peekingRegistrar;

    @Autowired
    public MonitoringService(RestTemplate restTemplate, ThreadPoolTaskScheduler threadPoolTaskScheduler, PeekingRegistrar peekingRegistrar) {
		this.restTemplate = restTemplate;
		this.threadPoolTaskScheduler = threadPoolTaskScheduler;
		this.peekingRegistrar = peekingRegistrar;
	}

    /**
     * Single status check
     * @param peeking specification {url, rate}
     */
	public void ping(PeekingDTO peeking) {
		AvailabilityDTO availability = restTemplate.getForObject(peeking.getUrl(), AvailabilityDTO.class);
		LOGGER.info("ping - {} : {}", peeking.getUrl(), availability.toString());
	}

	/**
	 * Start monitoring the given URL with the specified rate.
	 * To change the rate, first issue stop command, then start with a different rate.
	 * @param peeking specification {url, rate}
	 */
    public void start(PeekingDTO peeking) {
    	
    	String url = peeking.getUrl();
    	if (StringUtils.isEmpty(url)) {
			LOGGER.info("start - empty URL");
    		return;    		
    	}
    	
		LOGGER.info("start - monitoring URL: {}", url);

		if(peekingRegistrar.getUrlScheduledFutureMap().containsKey(url)) {
			LOGGER.info("start - Already scrutinizing URL: {} . Stop then start with a new rate for same URL", url);
    		return;
    	}
    	
    	Runnable task = () -> {
			    	AvailabilityDTO availability = restTemplate.getForObject(peeking.getUrl(), AvailabilityDTO.class);
			    	LOGGER.info("start - {} : {}", peeking.getUrl(), availability.toString());
			    	
	    			// update current (last) peek window with status and Instant of status change, if any
			    	String currentStatus = availability.getStatus();
	    			PeekWindow currentPeekWindow = retrieveCurrentPeekWindow(url);
	    			
		    		String reference;
		    		
			    	if (StringUtils.isEmpty(currentPeekWindow.getPreviousStatus())) {
			    		// first ping: previous state was null, record a flip if status != READY
			    		reference = READY_STATE;			    		
			    	} else {
		    			// other ping: record a flip if currentStatus different from previousStatus
		    			reference = currentPeekWindow.getPreviousStatus();
			    	}
			    	
			    	if (!StringUtils.isEmpty(reference) && !reference.equalsIgnoreCase(currentStatus)) {
			    		LOGGER.info("start - reference {} : currentStatus {}", reference, currentStatus);
			    		Instant flipInstant = Instant.now();
			    		currentPeekWindow.getFlips().add(flipInstant);   		
			    	}
		    		currentPeekWindow.setPreviousStatus(currentStatus);
    	};
    	
    	ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(task, peeking.getRate());
    	LOGGER.info("start - scheduledFuture.isDone: {}, scheduledFuture.isCancelled: {} ", 
    				scheduledFuture.isDone(), scheduledFuture.isCancelled());
    	
    	// for given URL register the scheduledFuture (to be able to cancel it when stop requested), rate and PeekWindow (start)
    	peekingRegistrar.getUrlScheduledFutureMap().putIfAbsent(url, scheduledFuture);
    	peekingRegistrar.getUrlRateMap().put(url, peeking.getRate());
    	
		// initialize status summary: {start/stop Instant, list of Instants when the status changed}
    	PeekWindow peekWindow = new PeekWindow();
    	Instant now = Instant.now();
		peekWindow.setStartInstant(now);
		
		List<PeekWindow> peekWindowList = peekingRegistrar.getUrlPeekWindowMap().get(url); 
		if (CollectionUtils.isEmpty(peekWindowList)) {
			peekWindowList = new ArrayList<>();
		}
		peekWindowList.add(peekWindow);
    	
		peekingRegistrar.getUrlPeekWindowMap().put(url, peekWindowList);
    	
    	LOGGER.info("start - Under scrutiny: {} | {}", 
    				peekingRegistrar.getUrlScheduledFutureMap().mappingCount(), peekingRegistrar.getUrlRateMap().mappingCount());
    }

    /**
     * Stop monitoring the given URL.
     * @param peeking specification {url [, rate]}
     */
    public void stop(PeekingDTO peeking) {
    	String url = peeking.getUrl();
    	if (StringUtils.isEmpty(url)) {
			LOGGER.info("stop - empty URL");
    		return;    		
    	}
    	
		LOGGER.info("stop - monitoring URL: {}", url);

    	ScheduledFuture<?> scheduledFuture = peekingRegistrar.getUrlScheduledFutureMap().get(url);

    	if (scheduledFuture == null) {
    		LOGGER.info("stop - scheduledFuture missing for URL: {}", url);
    	} else {	
    		Instant now = Instant.now();
    		LOGGER.info("stop - scheduledFuture.isDone: {}, scheduledFuture.isCancelled: {}", 
    					scheduledFuture.isDone(), scheduledFuture.isCancelled());

    		if(!scheduledFuture.isCancelled()) {
    			scheduledFuture.cancel(false);

    			peekingRegistrar.getUrlScheduledFutureMap().remove(url, scheduledFuture); // both key AND value should match for removal
    			peekingRegistrar.getUrlRateMap().remove(url);
    			
    			// update current (last) peek window with the stop instant
    			PeekWindow currentPeekWindow = retrieveCurrentPeekWindow(url);
    			currentPeekWindow.setStopInstant(now);
    		}

    		LOGGER.info("stop - Under scrutiny: {} | {}", 
    					peekingRegistrar.getUrlScheduledFutureMap().mappingCount(), peekingRegistrar.getUrlRateMap().mappingCount());
    	}

    }

    /**
     * Retrieve the result of the status monitoring for the given URL
     * @param url
     * @return monitoring summary
     */
	public List<PeekWindow> status(String url) {
    	if (StringUtils.isEmpty(url)) {
			LOGGER.info("status - empty URL");
    		return Collections.emptyList();    		
    	}
    	
		if (peekingRegistrar.getUrlPeekWindowMap().mappingCount() == 0) {
			LOGGER.info("status - monitoring URL: {}, no status accumulated yet", url);
    		return Collections.emptyList();    					
		}

		List<PeekWindow> peekWindowList = peekingRegistrar.getUrlPeekWindowMap().get(url);
		LOGGER.info("status - monitoring URL: {} of total: {}, status: {}", 
					url, peekingRegistrar.getUrlPeekWindowMap().mappingCount(), peekWindowList.toString());
		return peekWindowList;
	}

	/**
	 * Obtain the last PeekWindow, which should be updated with the current monitoring status
	 * @param url
	 * @return PeekWindow
	 */
	private PeekWindow retrieveCurrentPeekWindow(String url) {
		List<PeekWindow> peekWindowList = peekingRegistrar.getUrlPeekWindowMap().get(url);		
		int n = peekWindowList.size();
		PeekWindow currentPeekWindow = peekWindowList.get(n-1);
		return currentPeekWindow;
	}
}
