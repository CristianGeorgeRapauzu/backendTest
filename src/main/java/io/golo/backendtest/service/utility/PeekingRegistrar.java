package io.golo.backendtest.service.utility;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.stereotype.Component;

/**
 * Registrar for monitoring activity of URLs (the map key), 
 * assuming there are several servers to monitor.
 * 
 * Tracks:
 * 1. ScheduledFuture - to be able to cancel it on stop command
 * 2. rate of calls
 * 3. PeekWindow {start/stop Instant, list of Instants when the status changed}
 */
@Component
public class PeekingRegistrar {

	private ConcurrentHashMap<String, ScheduledFuture<?>> urlScheduledFutureMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Long> urlRateMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, List<PeekWindow>> urlPeekWindowMap = new ConcurrentHashMap<>();

	public ConcurrentHashMap<String, ScheduledFuture<?>> getUrlScheduledFutureMap() {
		return urlScheduledFutureMap;
	}
	
	public ConcurrentHashMap<String, Long> getUrlRateMap() {
		return urlRateMap;
	}

	public ConcurrentHashMap<String, List<PeekWindow>> getUrlPeekWindowMap() {
		return urlPeekWindowMap;
	}

}
