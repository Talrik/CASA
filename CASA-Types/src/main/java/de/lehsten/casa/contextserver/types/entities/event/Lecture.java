package de.lehsten.casa.contextserver.types.entities.event;

import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class Lecture extends StudIPEvent{
	
	private Room room = null;
	
	public Lecture(String title, Long begin, Long end, String StudIP_ID){
		super(title, begin, end, StudIP_ID);
		this.getProperties().put("title", title);
		this.getProperties().put("begin", begin.toString());
		this.getProperties().put("end", end.toString());
		this.getProperties().put("StudIP_ID", StudIP_ID);
		super.setisAssigned(false);
	}
	public Lecture(){
		super();
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		this.getProperties().put("room", room.getResource_id());
	}
	
}
