package de.lehsten.casa.contextserver.types;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Entity implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7054932600473079706L;
	private HashMap<String,String> properties = new HashMap<String,String>();
	private String source = "";

	public HashMap<String, String> getProperties() { 
		return properties;
	}
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
		updateHash();
	}
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
		updateHash();
	}
	public void updateProperty(String key1, String key2, String value2){
		this.properties.remove(key1);
		this.properties.put(key2, value2);
		updateHash();
	}
	public void updatePropertyKey(String key1, String key2){
		this.properties.put(key2, this.properties.get(key1));
		this.properties.remove(key1);
		updateHash();
	}
	public String getSource() {
		return source;
	}

	public void setSource(String Data_Source) {
		this.source = Data_Source;
		updateHash();
	}
	
	@Override
	public Entity clone(){
		try
	    {
	      Entity temp =  (Entity) super.clone();
	      temp.setProperties((HashMap<String, String>) this.getProperties().clone());
	      return temp;
	    }
	    catch ( CloneNotSupportedException e ) {
	      throw new InternalError();
	    }

	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (!(obj instanceof Entity)){
			return false;
		}
		Entity testEnt = (Entity)obj;
		return 	getProperties().equals(testEnt.getProperties()) &&
				getSource().equals(testEnt.getSource());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result  + getClass().hashCode();
		String id = getProperties().get("ID");
		result = 31 * result  + getProperties().hashCode();
		this.properties.put("ID", id);
		result = 31 * result  + getSource().hashCode();
		return result;
	}
	
	private void updateHash(){
		this.properties.remove("ID");
		this.properties.put("ID", String.valueOf(hashCode()));
	}
	
	@Override
	public String toString(){
		return getProperties().toString();
	}
	
}
