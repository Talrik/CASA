package de.lehsten.casa.utilities.services;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import java.io.Serializable;

public class ServiceProxyImp
  implements ServiceProxy, Serializable
{
  private String query;
  private Object[] params;
  private Class<? extends Service> servieType;
  private String title;
  private String description;
  
  public ServiceProxyImp() {}
  
  public ServiceProxyImp(String query, Object[] params) {}
  
  public Object[] getParams()
  {
    return this.params;
  }
  
  public void setParams(Object[] params)
  {
    this.params = params;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public void setQuery(String query)
  {
    this.query = query;
  }
  
  public Class<? extends Service> getServieType()
  {
    return this.servieType;
  }
  
  public void setServieType(Class<? extends Service> servieType)
  {
    this.servieType = servieType;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
}
