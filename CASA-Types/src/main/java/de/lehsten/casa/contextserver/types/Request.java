package de.lehsten.casa.contextserver.types;

import java.util.ArrayList;

public class Request extends Entity {

	ArrayList<Entity> results = new ArrayList<Entity>();
	ArrayList<Entity> restrictions = new ArrayList<Entity>();

	String requestId;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String id) {
		this.requestId = id;
		this.getProperties().put("RequestId", id);

	}
	public ArrayList<Entity> getRestrictions() {
		return restrictions;
	}
	public void setRestrictions(ArrayList<Entity> restrictions) {
		this.restrictions = restrictions;
	}
	public ArrayList<Entity> getResults() {
		return results;
	}
	public void setResults(ArrayList<Entity> results) {
		this.results =  results;
	}
}
