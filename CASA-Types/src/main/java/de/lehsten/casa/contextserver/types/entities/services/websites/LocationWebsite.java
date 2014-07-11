package de.lehsten.casa.contextserver.types.entities.services.websites;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.entities.place.Place;

@XmlRootElement
public class LocationWebsite extends Website{
	
	private ArrayList<Place> places = new ArrayList<Place>();	
	public LocationWebsite(){
		super();
	}
	
	public LocationWebsite(Place place){
		this.places.add(place);
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlace(Place place) {
		if(!places.contains(place)){
		this.places.add(place);
		this.getProperties().put("place", this.getProperties().get("place")+" "+place.getTitle());
		}
	}
	
	public void setPlaces(ArrayList<Place> places){
		for (Place p :places){
		this.setPlace(p);
		}
	}
}
