package de.lehsten.casa.contextserver.types;

import java.io.Serializable;
import java.util.Map;

public class Rule implements Serializable{
	
	private String packageName;
	private String name;
	private Map<String,Object> metaData;
	private String rule;
	private boolean isActive = true;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getMetaData() {
		return metaData;
	}
	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public boolean getIsActive() {
		return this.isActive;
	}
	public void setIsActive(boolean value) {
		this.isActive = value;
	}

}
