package de.lehsten.casa.contextserver.types.entities.device;

import java.util.ArrayList;
import java.util.Date;

import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.position.Position;

public class MobilePhone extends Phone{
	
	private Position location;
	private String os;
	ArrayList<Place> nearbyPlaces = new ArrayList<Place>();
	ArrayList<String> notifications = new ArrayList<String>();
	
	
	public ArrayList<String> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<String> notification) {
		this.notifications = notification;
	}
	
	public void addNotification(String notification) {
		this.notifications.add(notification);
		System.out.println(notification);
		}
	
	

	public ArrayList<Place> getNearbyPlaces() {
		return nearbyPlaces;
	}
	
	public void setNearbyPlaces(ArrayList<Place> nearbyPlaces) {
		this.nearbyPlaces = nearbyPlaces;
	}
	
	public void addToNearbyPlaces(Place nearbyPlace) {
		this.nearbyPlaces.add(nearbyPlace);
	}
	
	public Position getLocation() {
		return location;
	}

	public void setLocation(Position location) {
		this.location = location;
	}

	public String getOs() {
		return os;
	}
	
	public void setOS(String os){
		this.os = os;
		this.getProperties().put("os", os);
	}
	
}
