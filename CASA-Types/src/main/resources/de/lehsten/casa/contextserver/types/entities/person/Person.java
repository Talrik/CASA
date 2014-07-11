package de.lehsten.casa.contextserver.types.entities.person;

import java.io.Serializable;
import java.util.ArrayList;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;

public class Person extends Entity implements Serializable{
	
	private  String username;
	private  String studip_user_id;
	private  String firstname;
	private  String lastname;
	private  String email;
	private  ArrayList<String> rights = new ArrayList<String>();
	private  MobilePhone activeDevice;
	

	public MobilePhone getActiveDevice() {
		return activeDevice;
	}

	public void setActiveDevice(MobilePhone activeDevice) {
		this.activeDevice = activeDevice;
	}

	public Person(){
	}
	
	public Person(String username,String StudIP_user_id, String firstname, String lastname, String email, String rights){
		this.username = username;
		this.studip_user_id = StudIP_user_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.rights.add(rights);
	}
	
	public String getName(){
		return this.getFirstname() + " " + this.getLastname();
	}
	
	public String getUsername() {  
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
 
	public String getFirstname() { 
		return "Tom";
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<String> getRights() {
		return rights;
	}

	public void setRights(ArrayList<String> rights) {
		this.rights = rights;
	}

	public String getstudip_user_id() {
		return studip_user_id;
	}

	public void setstudip_user_id(String studIP_user_id) {
		studip_user_id = studIP_user_id;
	}

	

}
