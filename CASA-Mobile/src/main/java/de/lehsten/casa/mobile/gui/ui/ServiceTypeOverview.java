package de.lehsten.casa.mobile.gui.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.data.ServiceContainer;
import de.lehsten.casa.mobile.data.ServiceHandler;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;

public class ServiceTypeOverview extends NavigationView{
	
	public ServiceTypeOverview(final MainNavigationManager nav, ServiceContainer sc){
		
		VerticalComponentGroup servicesGroup = new VerticalComponentGroup();
		
		for (Service s: sc.getItemIds()){
			if (s instanceof LocationWebsite){
				final LocationWebsite lw = (LocationWebsite)s;
				NavigationButton service = new NavigationButton(lw.getTitle());
				service.setIcon(new ThemeResource("img/maps-icon.png"));
				servicesGroup.addComponent(service);
				service.addListener(new Button.ClickListener(){

					@Override
					public void buttonClick(ClickEvent event) {
						if (nav instanceof SmartphoneMainView){
							if (lw.getSubServices() == null && lw.getTargetURL() != null){
							((SmartphoneMainView) nav).setService(lw, null);}
							else if(lw.getSubServices() != null){
								ServiceContainer tempsc = new ServiceContainer();
								tempsc.addAll(lw.getSubServices());
				                ServiceTypeOverview v = new ServiceTypeOverview(nav, tempsc);
				                nav.navigateTo(v);
								}
							}
						}
					}
					
				 );
			}
			else if (s instanceof EventWebsite){ 
				final EventWebsite ew = (EventWebsite)s;
				NavigationButton service = new NavigationButton(ew.getTitle());
				service.setIcon(new ThemeResource("img/Actions-help-hint-icon.png"));
				servicesGroup.addComponent(service);
				service.addListener(new Button.ClickListener(){

					@Override
					public void buttonClick(ClickEvent event) {
						if (nav instanceof SmartphoneMainView){
							((SmartphoneMainView) nav).setService(ew, null);
						}
					}
					
				} );
			}
			else if (s instanceof Website){
				final Website w = (Website)s;
				NavigationButton service = new NavigationButton(w.getTitle());
				service.setIcon(new ThemeResource("img/Categories-preferences-desktop-personal-icon.png"));
				servicesGroup.addComponent(service);
				service.addListener(new Button.ClickListener(){

					@Override
					public void buttonClick(ClickEvent event) {
						if (nav instanceof SmartphoneMainView){
							((SmartphoneMainView) nav).setService(w, null);
						}
					}
					
				} );
			}
		
	}
		this.setContent(servicesGroup);
		this.setToolbar(ServiceOverview.createToolbar());
	}

}
