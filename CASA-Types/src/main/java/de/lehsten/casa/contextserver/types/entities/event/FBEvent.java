package de.lehsten.casa.contextserver.types.entities.event;

import de.lehsten.casa.contextserver.types.position.Position;

public class FBEvent extends Event{

	private String FB_ID;
	private String location;
	private Position position;
	
	public FBEvent(){
		super();
	}
	
	
	public String getFB_ID() {
		return FB_ID;
	}
	public void setFB_ID(String FB_ID) {
		this.FB_ID = FB_ID;
		this.getProperties().put("FB_ID", FB_ID);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
		this.getProperties().put("location", location);
		
		position = (new Position()).getPositionFromString(location);
		this.getProperties().put("latitude", String.valueOf(position.getLat()));
		this.getProperties().put("longitude", String.valueOf(position.getLon()));
		
	}
	
	public Position getPosition(){
		return position;
	}
	
	
	@Override
	public String toString() {
		return FB_ID + "; " + location + " (" + position +")" + "; " + this.getTitle() + "; " + this.getBeginDate() + "; " + this.getEndDate();
	}
	
}
