package waldo.model;

import java.util.Date;
import waldo.util.LatLon;

/**
 * Represents a Waldo
 */

public class Waldo {

	// The name the Waldo goes by
	private String name;
	
	// When was this Waldo last updated?
	private Date lastUpdated;
	
	// The location of the Waldo
	private LatLon lastLocation;

	/**
	 * Constructor
	 * 
	 * @param name The name of the Waldo
	 * @param lastUpdated The time Waldo was last updated
	 * @param lastLocation Where was Waldo?
	 */
	public Waldo(String name, Date lastUpdated, LatLon lastLocation) {
		this.name = name;
		this.lastUpdated = lastUpdated;
		this.lastLocation = lastLocation;
	}

	/**
	 * Return Waldo's name
	 * @return The name of Waldo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return Waldo's last update time
	 * @return The time Waldo was last updated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Update the last updated time of Waldo
	 * @param date The time to remember Waldo was last updated
	 */
	public void update(Date date) {
		this.lastUpdated = date;
	}

	/**
	 * Update the location of Waldo
	 * @param latlon The last location of Waldo
	 */
	public void update(LatLon latlon) {
		this.lastLocation = latlon;
	}
	
	/**
	 * Retrieve Waldo's location
	 * @return Waldo's location
	 */
	public LatLon getLastLocation() {
		return lastLocation;
	}

	@Override
	public String toString() {
		return name + " " + lastLocation + " " + lastUpdated;
	}
	
}
