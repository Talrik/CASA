package de.lehsten.casa.mobile.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.event.dom.client.DomEvent.Type;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class ServiceContainer extends BeanItemContainer<Service> {

    private static final long serialVersionUID = 1L;
    
    public ServiceContainer(){
    	super(Service.class);
    }
    
    
    public Collection<? extends Service> getServices(Object o){
    	List<Service> services = new ArrayList<Service>();
    	for (Service service : getAllItemIds()){
    		if (true){
    			services.add(service);
    		}
    	}
		return services;
    }
    
    public Collection<? extends Service> getServiceTypes(){
    	List<Service> serviceTypes = new ArrayList<Service>();
    		serviceTypes.add(new LocationWebsite());
    		serviceTypes.add(new EventWebsite());
    		serviceTypes.add(new Website());
    	return serviceTypes;
    }

	
}
