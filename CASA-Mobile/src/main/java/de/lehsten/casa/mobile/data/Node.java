package de.lehsten.casa.mobile.data;

import de.lehsten.casa.contextserver.types.entities.device.Device;

public class Node extends Device{
	
	private String developer;
	private String requiredVersionOfCASATypes;
	private String description;
	private String domainType;
	private String domainName;
	private String endpoint;
	private boolean usePosition = false;
	
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
		this.getProperties().put("developer", developer);
	}
	public String getRequiredVersionOfCASATypes() {
		return requiredVersionOfCASATypes;
	}
	public void setRequiredVersionOfCASATypes(String requiredVersionOfCASATypes) {
		this.requiredVersionOfCASATypes = requiredVersionOfCASATypes;
		this.getProperties().put("requiredVersionOfCASATypes", requiredVersionOfCASATypes);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		this.getProperties().put("description", description);
	}
	public String getDomainType() {
		return domainType;
	}
	public void setDomainType(String domainType) {
		this.domainType = domainType;
		this.getProperties().put("domainType", domainType);
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
		this.getProperties().put("domainName", domainName);
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		this.getProperties().put("endpoint", endpoint);
	}
	public boolean isUsePosition() {
		return usePosition;
	}
	public void setUsePosition(boolean usePosition) {
		this.usePosition = usePosition;
	}

}
