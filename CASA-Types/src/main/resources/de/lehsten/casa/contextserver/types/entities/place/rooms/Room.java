package de.lehsten.casa.contextserver.types.entities.place.rooms;

import java.io.Serializable;
import java.util.HashMap;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Building;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class Room extends Place implements Serializable{
	
	private String resource_id = null;
	private Building building;
	
	
	public Room(){}
	public Room(String id, String title){
		this.resource_id = id;
		this.setTitle(title);
	}
	
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}

	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	
}
