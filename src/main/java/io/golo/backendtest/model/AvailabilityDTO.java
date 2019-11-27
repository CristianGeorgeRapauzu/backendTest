package io.golo.backendtest.model;

/**
 * Report the current status of the monitored server from
 * https://api.test.paysafe.com/accountmanagement/monitor
 * Example:
 * {"status":"READY"}
 */
public class AvailabilityDTO {

	private String status;
	
	public AvailabilityDTO() {
        // Default constructor, empty for Json serializer
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AvailabilityDTO [status=" + status + "]";
	}
	
}
