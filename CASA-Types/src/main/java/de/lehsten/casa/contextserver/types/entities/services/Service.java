package de.lehsten.casa.contextserver.types.entities.services;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.Entity;

@XmlRootElement
public class Service extends Entity{
	
	private String title; 
	private String description;
	private String provider;
	private ArrayList<String> restrictions = new ArrayList<String>();
	private ArrayList<Service> subServices = new ArrayList<Service>();
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		this.getProperties().put("title", title);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		this.getProperties().put("description", description);
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
		this.getProperties().put("provider", provider);
	}
	public ArrayList<String> getRestrictions() {
		return restrictions;
	}
	public void setRestrictions(ArrayList<String> restrictions) {
		this.restrictions = restrictions;
	}
	public void addRestrictions(Collection<String> restrictions) {
		this.restrictions.addAll(restrictions);
	}
	public void addRestriction(String restriction) {
		this.restrictions.add(restriction);
	}	
	public void removeRestrictions(ArrayList<String> restrictions) {
		this.restrictions.removeAll(restrictions);
	}
	public void removeRestriction(String restriction) {
		this.restrictions.remove(restriction);
	}
	public ArrayList<Service> getSubServices() {
		return subServices;
	}
	public void addSubService(Service s) {
		this.subServices.add(s);
	}
	public void addSubServices(Collection<Service> s) {
		this.subServices.addAll(s);
	}
	public void setSubServices(ArrayList<Service> subServices) {
		this.subServices = subServices;
	}
	
	

}
