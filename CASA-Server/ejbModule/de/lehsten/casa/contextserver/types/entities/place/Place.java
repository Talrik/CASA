package de.lehsten.casa.contextserver.types.entities.place;

import de.lehsten.casa.contextserver.types.Entity;

public class Place extends Entity{
	 String title;
	 private Double latitude; 
	 private Double longitude;
	 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getLatitude() {
		return this.latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude= latitude;
	}
	public Double getLongitude() {
		return this.longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
