package de.lehsten.casa.contextserver.types.entities.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class Event extends Entity implements Serializable{
	
	private String title;
	private Date beginDate;
	private Date endDate;
	ArrayList<String> resources = new ArrayList<String>();

	public Event(){
		
	}
	
	public String toString(){
		return title;
	}
	
	public Event(String title){
		this.setTitle(title);
	}
	
	public Event(String title, Date begin, Date end){
		this.setTitle(title);
		this.setBeginDate(begin);
		this.setEndDate(end);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.getProperties().put("title", title);
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date begin) {
		this.beginDate = begin;
		this.getProperties().put("beginDate", begin.toString());
	}
	
	public void setBeginDate(Long begin) {
		this.beginDate = new Date(begin*1000);
		this.getProperties().put("beginDate", this.beginDate.toString());
	}
	
	

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date end) {
		this.endDate = end;
		this.getProperties().put("endDate", end.toString());
	}

	public void setEndDate(Long end) {
		this.endDate = new Date(end * 1000);
		this.getProperties().put("endDate", this.endDate.toString());
	}


	
	public ArrayList<String> getResources() {
		return resources;
	}

	public void setResources(ArrayList<String> resources) {
		this.resources = resources;
		this.getProperties().put("resources","");
		for (String r : resources){
				this.getProperties().put("resources", this.getProperties().get("resources") +" "+r);
		}
		
	}
	public void addResource(String resource) {
		this.resources.add(resource);
		if (this.getProperties().containsKey("resources")){
		this.getProperties().put("resources", (this.getProperties().get("resources") + resource));
		}else
		{
		this.getProperties().put("resources", resource);	
		}
	}

}
