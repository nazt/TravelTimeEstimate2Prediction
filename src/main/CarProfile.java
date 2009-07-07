package main;

public class CarProfile implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;
	
	private GPoint pastpoint;
	private GPoint startpoint;
	
	public CarProfile(GPoint pastpoint, GPoint startpoint) {
		super();
		this.pastpoint = pastpoint;
		this.startpoint = startpoint;
	}
	/**
	 * @return the pastpoint
	 */
	public GPoint getPastpoint() {
		return pastpoint;
	}
	/**
	 * @return the startpoint
	 */
	public GPoint getStartpoint() {
		return startpoint;
	}
	/**
	 * @param pastpoint the pastpoint to set
	 */
	public void setPastpoint(GPoint pastpoint) {
		this.pastpoint = pastpoint;
	}
	/**
	 * @param startpoint the startpoint to set
	 */
	public void setStartpoint(GPoint startpoint) {
		this.startpoint = startpoint;
	}
	
}
