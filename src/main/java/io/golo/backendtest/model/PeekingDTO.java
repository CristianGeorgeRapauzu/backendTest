package io.golo.backendtest.model;

/**
 * Specify the monitoring activity of the URL with the specified rate (milliseconds).
 */
public class PeekingDTO {

	private String url;
	private long rate;

    public PeekingDTO() {
        // Default constructor, empty for Json serializer
    }

	public String getUrl() {
		return url;
	}

    public void setUrl(String url) {
		this.url = url;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "PeekingDTO[url=" + url + 
				", rate=" + rate + "]";
	}

}
