package de.lehsten.casa.utilities.services;

import de.lehsten.casa.contextserver.types.entities.services.Service;

public abstract interface ServiceProxy
{
  public abstract Object[] getParams();
  
  public abstract void setParams(Object[] paramArrayOfObject);
  
  public abstract String getQuery();
  
  public abstract void setQuery(String paramString);
  
  public abstract Class<? extends Service> getServieType();
  
  public abstract void setServieType(Class<? extends Service> paramClass);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setTitle(String paramString);
}
