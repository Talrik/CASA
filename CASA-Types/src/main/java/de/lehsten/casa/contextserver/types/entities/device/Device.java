package de.lehsten.casa.contextserver.types.entities.device;

import java.io.Serializable;
import java.util.ArrayList;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.person.Person;

public class Device extends Entity implements Serializable{
	
	public boolean isMobile; 
	public boolean isMounted;
	private String deviceID;
	private String title; 
	ArrayList<String> functionalities = new ArrayList<String>();

	
	public ArrayList<String> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(ArrayList<String> functionalities) {
		this.functionalities = functionalities;
	}

	public Device(){};
	
	public Device(String id, String title){
		this.deviceID = id;
		this.title = title;
	};
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	public boolean isMobile() {
		return isMobile;
	}
	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}
	public boolean isMounted() {
		return isMounted;
	}
	public void setMounted(boolean isMounted) {
		this.isMounted = isMounted;
	}
	

}
