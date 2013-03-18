package de.lehsten.casa.contextserver.types.entities.event;

import java.io.Serializable;
import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class Event extends Entity implements Serializable{
	
	private String title;
	private String StudIP_ID;
	private Long begins;
	private Long ends;
	private boolean isAssigned = true;
	ArrayList<String> resources = new ArrayList<String>();

	public Event(){
		
	}
	
	public String toString(){
		return title;
	}
	
	public Event(String title){
		this.title = title;
	}
	
	public Event(String title, Long begin, Long end, String StudIP_ID){
		this.title = title;
		this.begins = begin;
		this.ends = end;
		this.StudIP_ID = StudIP_ID;
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getBegin() {
		return begins;
	}

	public void setBegin(Long begin) {
		this.begins = begin;
	}

	public Long getEnd() {
		return ends;
	}

	public void setEnd(Long end) {
		this.ends = end;
	}

	
	public String getStudIP_ID() {
		return this.StudIP_ID;
	}

	public void setStudIP_ID(String studIP_ID) {
		this.StudIP_ID = studIP_ID;
	}

	public boolean getisAssigned() {
		return isAssigned;
	}

	public void setisAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	public ArrayList<String> getResources() {
		return resources;
	}

	public void setResources(ArrayList<String> resources) {
		this.resources = resources;
	}
	public void addResource(String resource) {
		this.resources.add(resource);
	}

}
