package de.lehsten.casa.contextserver.types.entities.place;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.Entity;

/**Defines a place with a title and longitude and latitude coordinates.
 * @author phil
 * @see Entity
 */
@XmlRootElement
public class Place extends Entity{
	
	 private String title;
	 private Double latitude; 
	 private Double longitude;
	 
	/**
	 * @return title as {@link String}
	 */
	public String getTitle() {
		return title;
	}
	
	/** Sets the title of the {@link Place}.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		this.getProperties().put("title", title);
	}
	/**
	 * @return latitude as {@link Double}
	 */
	public Double getLatitude() {
		return this.latitude;
	}
	
	/**Sets latitude of a {@link Place}
	 * @param latitude 
	 */
	public void setLatitude(Double latitude) {
		this.latitude= latitude;
		this.getProperties().put("latitude", latitude.toString());
	}
	
	/**
	 * @return longitude as {@link Double}
	 */
	public Double getLongitude() {
		return this.longitude;
	}
	
	/**Sets longitude of a {@link Place}
	 * @param longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
		this.getProperties().put("longitude", longitude.toString());
	}
}
