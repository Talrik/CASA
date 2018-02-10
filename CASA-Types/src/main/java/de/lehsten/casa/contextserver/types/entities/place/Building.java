package de.lehsten.casa.contextserver.types.entities.place;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

@XmlRootElement
public class Building extends Place{

	ArrayList<Room> rooms = new ArrayList<Room>();
	ArrayList<String> roomsID = new ArrayList<String>();
	String buildingID;
	String type;

	String Address;
	
	public Building(){}
	
	public Building(String buildingID, String title){
		this.buildingID = buildingID;
		this.setTitle(title); 
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
	public void addRoom(Room r) {
		this.rooms.add(r);
	}
	
	public ArrayList<String> getRoomsID() {
		return roomsID;
	}
	public void setRoomsID(ArrayList<String> roomsID) {
		this.roomsID = roomsID;
	}
	public void addRoomID(String roomsID) {
		this.roomsID.add(roomsID);
	}
	
	
	
	public String getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}	
	
	

}
