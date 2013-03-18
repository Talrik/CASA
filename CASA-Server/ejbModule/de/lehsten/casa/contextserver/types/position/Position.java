package de.lehsten.casa.contextserver.types.position;

import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.Context;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class Position extends Context{
	
	Float lat; 
	Float lon;
	
	
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}

}
