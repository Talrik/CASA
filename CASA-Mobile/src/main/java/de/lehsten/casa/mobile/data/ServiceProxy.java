package de.lehsten.casa.mobile.data;

import de.lehsten.casa.contextserver.types.entities.services.Service;

public interface ServiceProxy {
	
	public Object[] getParams();
	public void setParams(Object[] params);
	public String getQuery();
	public void setQuery(String query);
	public Class<? extends Service> getServieType();
	public void setServieType(Class<? extends Service> servieType);
	public String getDescription();
	public void setDescription(String description);
	public String getTitle();
	public void setTitle(String title);
}
