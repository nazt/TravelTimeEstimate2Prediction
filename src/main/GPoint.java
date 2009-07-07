package main;

import java.sql.Timestamp;

public class GPoint implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;
	
	private String geom;
	private double heading;
	private double lat;
	private double lng;
	private Timestamp rectime;
	private int refline;
	private double speed;
	private int vid;
	
	
	public GPoint(double heading, double lat, double lng, Timestamp rectime,
			double speed, int vid) {
		super();
		this.heading = heading;
		this.lat = lat;
		this.lng = lng;
		this.rectime = rectime;
		this.speed = speed;
		this.vid = vid;
	}

	/**
	 * @return the geom
	 */
	public String getGeom() {
		return geom;
	}
	/**
	 * @return the heading
	 */
	public double getHeading() {
		return heading;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @return the rectime
	 */
	public Timestamp getRectime() {
		return rectime;
	}
	/**
	 * @return the refline
	 */
	public int getRefline() {
		return refline;
	}
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @return the vid
	 */
	public int getVid() {
		return vid;
	}
	/**
	 * @param geom the geom to set
	 */
	public void setGeom(String geom) {
		this.geom = geom;
	}
	/**
	 * @param heading the heading to set
	 */
	public void setHeading(double heading) {
		this.heading = heading;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * @param rectime the rectime to set
	 */
	public void setRectime(Timestamp rectime) {
		this.rectime = rectime;
	}
	/**
	 * @param refline the refline to set
	 */
	public void setRefline(int refline) {
		this.refline = refline;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/**
	 * @param vid the vid to set
	 */
	public void setVid(int vid) {
		this.vid = vid;
	}
	
}
