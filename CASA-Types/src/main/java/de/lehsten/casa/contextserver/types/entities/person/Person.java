package de.lehsten.casa.contextserver.types.entities.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.device.Device;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;

@XmlRootElement
public class Person extends Entity implements Serializable{
	
	private  String CASAUsername;
	private  String firstname;
	private  String lastname;
	private  String email;
	private  ArrayList<String> rights = new ArrayList<String>();
	private  ArrayList<Device> activeDevices = new ArrayList<Device>();
	private  ArrayList<Identity> identities = new ArrayList<Identity>();
	

	public  ArrayList<Device> getActiveDevices() {
		return activeDevices;
	}

	public void setActiveDevice(ArrayList<Device> activeDevice) {
		this.activeDevices = activeDevice;
	}

	public Person(){
	}
	
	/**
	 * @param username			user name for this CASA node
	 * @param firstname			first name of this {@link Person}
	 * @param lastnam			last name of this {@link Person}
	 * @param email				email of this {@link Person}
	 * @param rights			{@link Collection} of rights
	 * @see Entity 
	 */
	public Person(String username, String firstname, String lastname, String email, Collection<String> rights){
		this.setCASAUsername(username);
		this.setFirstname(firstname);
		this.setLastname(lastname);
		this.setEmail(email);
		this.setRights((ArrayList<String>) rights);
	}
	
	public String getName(){
		return this.getFirstname() + " " + this.getLastname();
	}
	
	public String getCASAUsername() {  
		return CASAUsername;
	}

	public void setCASAUsername(String CASAUsername) {
		this.CASAUsername = CASAUsername;
		this.getProperties().put("CASAUsername", CASAUsername);
	}
 
	public String getFirstname() { 
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
		this.getProperties().put("firstname", firstname);
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
		this.getProperties().put("lastname", lastname);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		this.getProperties().put("email", email);
	}

	public ArrayList<String> getRights() {
		return rights;
	}

	public void setRights(ArrayList<String> rights) {
		this.rights = rights;
		this.getProperties().put("rights","");
		for (String r : rights){
			this.getProperties().put("rights", this.getProperties().get("rights") +" "+r);
	}
	}

	public ArrayList<Identity> getIdentities() {
		return identities;
	}

	public void setIdentitys(ArrayList<Identity> identities) {
		this.identities = identities;
		this.getProperties().put("identities","");
		for (Identity i : identities){
			this.getProperties().put("identites", this.getProperties().get("identities") +" "+i.getIdentityUserName()+"@"+i.getIdentityDomain());
	}
	}

	

}
