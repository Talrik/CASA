package de.lehsten.casa.mobile.gui.ui;

import java.util.ArrayList;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.data.ServiceHandler;

public class ServiceOverview extends NavigationView{
	
	public ServiceOverview(){
		
		VerticalComponentGroup services = new VerticalComponentGroup();
		//Lookup "relevant" services
		ServiceHandler sh = new ServiceHandler();
		ArrayList<Service> serviceList = sh.getAllServices();
		for (Service s: serviceList){
				if (s instanceof LocationWebsite){
					LocationWebsite lw = (LocationWebsite)s;
					NavigationButton service = new NavigationButton(lw.getTitle());
					service.setIcon(new ThemeResource("img/maps-icon.png"));
					services.addComponent(service);
				}
				else if (s instanceof EventWebsite){
					EventWebsite lw = (EventWebsite)s;
					NavigationButton service = new NavigationButton(lw.getTitle());
					service.setIcon(new ThemeResource("img/Actions-help-hint-icon.png"));
					services.addComponent(service);
				}
				else if (s instanceof Website){
					Website lw = (Website)s;
					NavigationButton service = new NavigationButton(lw.getTitle());
					service.setIcon(new ThemeResource("img/Categories-preferences-desktop-personal-icon.png"));
					services.addComponent(service);
				}
			
		}
		//
		
		NavigationButton service1 = new NavigationButton("Service 1");
		service1.setIcon(new ThemeResource("img/Actions-chronometer-icon.png"));
		services.addComponent(service1);		
		
		NavigationButton service3 = new NavigationButton("Service 3");
		service3.setIcon(new ThemeResource("img/Actions-help-hint-icon.png"));
		services.addComponent(service3);
		
		NavigationButton service4 = new NavigationButton("Service 4");
		service4.setIcon(new ThemeResource("img/Categories-preferences-desktop-personal-icon.png"));
		services.addComponent(service4);
		
		this.setContent(services);
	}

}
