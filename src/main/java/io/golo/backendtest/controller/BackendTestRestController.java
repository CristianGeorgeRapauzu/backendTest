package io.golo.backendtest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.golo.backendtest.model.PeekingDTO;
import io.golo.backendtest.service.MonitoringService;
import io.golo.backendtest.service.utility.PeekWindow;

/**
 * Rest controller exposing all API endpoints for status monitoring
 */
@RestController
@RequestMapping("/monitor")
public class BackendTestRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendTestRestController.class);

    private MonitoringService monitoringService;
    
    @Autowired
    public BackendTestRestController(MonitoringService monitoringService) {
    	this.monitoringService = monitoringService;
    }

    /**
     * Returns API resource
     *
     * @param parameter
     *
     * @return The response containing the data to retrieve.
     */
    @GetMapping(value = "/uri to setup here")
    public ResponseEntity<PeekingDTO> getData(@RequestParam(name = "parameter", required = false) String parameter) {
        LOGGER.trace("Get resource");


        return new ResponseEntity<>(new PeekingDTO(), HttpStatus.OK);
    }
    
	/**
	 * POST 
	 * 
	 * @param peeking {url, rate}
	 */
    @PostMapping(path="/ping", consumes="application/json")
    public void quote(@RequestBody PeekingDTO peeking) {
        LOGGER.trace("POST ping - url: {}", peeking.getUrl());

    	monitoringService.ping(peeking);
    }

	/**
	 * POST start - command to launch url monitoring tasks at the given rate (milliseconds)
	 * 
	 * @param peeking {url, rate}
	 */
   @PostMapping(path="/start", consumes="application/json")
    public void start(@RequestBody PeekingDTO peeking) {
        LOGGER.trace("POST start - url: {}", peeking.getUrl());

    	monitoringService.start(peeking);
    }

	/**
	 * POST stop - command to end monitoring tasks
	 * 
	 * @param peeking {url, rate}
	 */
   @PostMapping(path="/stop", consumes="application/json")
    public void stop(@RequestBody PeekingDTO peeking) {
        LOGGER.trace("POST stop - url: {}", peeking.getUrl());

    	monitoringService.stop(peeking);
    }
    
	/**
	 * GET status - command to retrieve the monitoring status summary
	 * 
	 * @param url
	 * 
     * @return list of peek windows (start-stop intervals) with a list of status change Instants
	 */
    @GetMapping("/status")
    public ResponseEntity<List<PeekWindow>> status(@RequestParam(value="url") String url) {
        LOGGER.trace("GET status - url: {}", url);

        List<PeekWindow> monitoringSummary = monitoringService.status(url);
		HttpStatus httpStatusCode = CollectionUtils.isEmpty(monitoringSummary)? HttpStatus.NO_CONTENT : HttpStatus.OK;
		
		return new ResponseEntity<>(monitoringSummary, httpStatusCode);
    }

}
