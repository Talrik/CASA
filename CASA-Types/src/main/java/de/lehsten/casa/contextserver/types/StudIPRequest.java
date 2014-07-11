package de.lehsten.casa.contextserver.types;

import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class StudIPRequest extends Request {
		
	ArrayList<Identity> roles = new ArrayList<Identity>();
	ArrayList<Event> lectures = new ArrayList<Event>();
	ArrayList<Place> locations = new ArrayList<Place>();
	
	
	public StudIPRequest(Request r){
		this.setRequestId(r.getRequestId());
		for (Entity e : r.getRestrictions()){
			if(e instanceof Event){
				lectures.add((Event)e);
			}
			if(e instanceof Place){
				locations.add((Place)e);
			}
			if(e instanceof StudIPIdentity){
				roles.add((StudIPIdentity)e);
			}
		}
		this.restrictions = r.restrictions;
		this.results  = r.results;
	}
	
	public ArrayList<Identity> getRoles() {
		return roles;
	}
	public void setRoles(ArrayList<Identity> roles) {
		this.roles = roles;
	}
	public ArrayList<Event> getLectures() {
		return lectures;
	}
	public void setLectures(ArrayList<Event> lectures) {
		this.lectures = lectures;
	}
	public ArrayList<Place> getLocations() {
		return locations;
	}
	public void setLocations(ArrayList<Place> locations) {
		this.locations = locations;
	}
	
	
}
