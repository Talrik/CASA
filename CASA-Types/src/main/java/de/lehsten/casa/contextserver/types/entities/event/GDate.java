package de.lehsten.casa.contextserver.types.entities.event;

/**
 * 
 * @author Stefan
 *
 */

public class GDate extends Event {

	private String location;
	private float longitude;
	private float latitude; 
	
	public GDate (){
		super();
	}

	public String toString(){
		String ausgabe = null;
		ausgabe = 	"Titel: " + this.getTitle() +"\n" ;
		return ausgabe;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
}
