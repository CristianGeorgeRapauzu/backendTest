package io.golo.backendtest.service.utility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Status monitoring summary:
 * {start/stop Instant, list of Instants when the status changed}
 */
public class PeekWindow {
	// when start/stop command is issued - define the observation window
	private Instant startInstant; 
	private Instant stopInstant;

	/* monitored state changes - Instant 2n+1: up, 2n+2: down
	   up periods: [2n+1, 2n+2|now]; 1 Instant: started with monitored server currently up
	   down periods: [2n+2, 2n+3|now]; empty list: started with monitored server currently down
	   even number of Instants: server is down currently
	   odd number of Instants: server is up currently
	 */
	private List<Instant> flips = new ArrayList<>();

	private String previousStatus;

	public PeekWindow(){	
	}
	
	public Instant getStartInstant() {
		return startInstant;
	}

	public void setStartInstant(Instant startInstant) {
		this.startInstant = startInstant;
	}

	public Instant getStopInstant() {
		return stopInstant;
	}

	public void setStopInstant(Instant stopInstant) {
		this.stopInstant = stopInstant;
	}

	public List<Instant> getFlips() {
		return flips;
	}

	public void setFlips(List<Instant> flips) {
		this.flips = flips;
	}
	
	public String getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}

	@Override
	public String toString() {
		return "PeekWindow [startInstant=" + startInstant
				+ ", stopInstant=" + stopInstant
				+ ", previousStatus=" + previousStatus
				+ ", flips=" + flips + "]";
	}


}
