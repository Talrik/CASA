package de.lehsten.casa.contextserver.types.entities.event;

import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class Lecture extends Event{
	
	private Room room = null;
	
	public Lecture(String title, Long begin, Long end, String StudIP_ID){
		super(title, begin, end, StudIP_ID);
		this.properties.put("title", title);
		this.properties.put("begin", begin.toString());
		this.properties.put("end", end.toString());
		this.properties.put("StudIP_ID", StudIP_ID);
		super.setisAssigned(false);
	}
	public Lecture(){
		super();
	}
	
	public boolean isSuper(){
		return true;
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		this.properties.put("room", room.getResource_id());
	}
	
}
