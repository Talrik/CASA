package de.lehsten.casa.contextserver.types;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Entity implements Serializable{

	public HashMap<String,String> properties = new HashMap<String,String>();
	private String source = null;

	public HashMap<String, String> getProperties() { 
		return properties;
	}
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}
	public void updateProperty(String key1, String key2, String value2){
		this.properties.remove(key1);
		this.properties.put(key2, value2);
	}
	public void updatePropertyKey(String key1, String key2){
		this.properties.put(key2, this.properties.get(key1));
		this.properties.remove(key1);
	}
	public String getSource() {
		return source;
	}

	public void setSource(String Data_Source) {
		this.source = Data_Source;
	}
	
}
