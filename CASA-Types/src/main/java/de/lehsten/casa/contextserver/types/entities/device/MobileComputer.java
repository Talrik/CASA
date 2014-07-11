package de.lehsten.casa.contextserver.types.entities.device;

import de.lehsten.casa.contextserver.types.entities.person.Person;

public class MobileComputer extends Computer{

	private Person owner;
	private  String studip_user_id;
	private String userAgent;
	private String accept;
	private float latitude;
	private float longitude;
	
	public Person getOwner() {
		return owner;
	}
	public void setOwner(Person owner) {
		this.owner = owner;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getStudip_user_id() {
		return studip_user_id;
	}
	public void setStudip_user_id(String studip_user_id) {
		this.studip_user_id = studip_user_id;
	}
	
}
