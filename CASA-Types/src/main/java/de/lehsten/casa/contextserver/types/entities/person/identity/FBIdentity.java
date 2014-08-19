package de.lehsten.casa.contextserver.types.entities.person.identity;

import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.entities.device.Device;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.position.Position;

public class FBIdentity extends Identity {
	
	private String fbUserId;
	private String fbUserName;
	private Position position;
	private ArrayList<Device> devices;
	
	public FBIdentity(){}
	
	public FBIdentity(String fbUserId) {
		this.setFbUserId(fbUserId);
		this.devices = new ArrayList<Device>();
	}

	
	
	/**
	 * @return
	 */
	public String getFbUserId() {
		return fbUserId;
	}
	/**
	 * @param fbUserId
	 */
	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
		this.getProperties().put("fbUserId", fbUserId);
	}
	
	public String getFbUserName() {
		return fbUserName;
	}
	public void setFbUserName(String fbUserName) {
		this.fbUserName = fbUserName;
		this.getProperties().put("fbUserName", fbUserName);
	}
	
	public void setPosition(Position position){
		this.position = position;
		this.getProperties().put("latitude", String.valueOf(position.getLatitude()));
		this.getProperties().put("longitude", String.valueOf(position.getLongitude()));
	}
	
	public void setPosition(Double lat, Double lon){
		this.position = new Position(lat, lon);
		this.getProperties().put("latitude", String.valueOf(lat));
		this.getProperties().put("longitude", String.valueOf(lon));
	}
	
	public Position getPosition(){
		return position;
	}
	
	public void addDevice(Device device) {
		this.devices.add(device);
		// TODO this.getProperties().put...
	}
	
	public ArrayList<Device> getDevices() {
		return devices;
	}
	
	
	public String toString(){
		String out = "ID: " + fbUserId
					+" UserName:  " + fbUserName
					+"; FirstName: " + this.getFirstname()
					+"; LastName: " + this.getLastname()
					+"; EMail: " + this.getEmail()
					+"; Location: " + this.getPosition();
		
		if (!devices.isEmpty()) {
			for (int i = 0; i<devices.size(); i++){
				Device dev = devices.get(i);
				out += "; Device: " + dev.getTitle();
				if (dev.getClass().isInstance(new MobilePhone())){
					MobilePhone phone= (MobilePhone) dev;
					out += "(" + phone.getOs() + ")";
				}
			}
		}
		
		return out;
	}
	
}
