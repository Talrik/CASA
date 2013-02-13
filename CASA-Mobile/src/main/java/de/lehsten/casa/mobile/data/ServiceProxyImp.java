package de.lehsten.casa.mobile.data;

import java.io.Serializable;

import de.lehsten.casa.contextserver.types.entities.services.Service;

public class ServiceProxyImp implements ServiceProxy, Serializable{

	private String query;
	private Object[] params;
	private Class<? extends Service> servieType;
	private String title; 
	private String description;
	
	public ServiceProxyImp(){
		
	}
	
	public ServiceProxyImp(String query, Object[] params){
		
	}
	
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public Class<? extends Service> getServieType() {
		return servieType;
	}

	public void setServieType(Class<? extends Service> servieType) {
		this.servieType = servieType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
