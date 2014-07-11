package de.lehsten.casa.contextserver.types.entities.applications.websites;

import de.lehsten.casa.contextserver.types.entities.place.Place;

public class LocationWebsite extends Website{

	public Place place;

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	} 
}
