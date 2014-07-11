package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.server.ThemeResource;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.data.ServiceContainer;

public class ServiceTypeOverview extends NavigationView{
	
	public ServiceTypeOverview(final MainNavigationManager nav, ServiceContainer sc){
		
		VerticalComponentGroup servicesGroup = new VerticalComponentGroup();
		
		for (Service s: sc.getItemIds()){
			if (s instanceof LocationWebsite){
				final LocationWebsite lw = (LocationWebsite)s;
				NavigationButton service = new NavigationButton(lw.getTitle());
				service.setIcon(new ThemeResource("../base/img/maps-icon.png"));
				servicesGroup.addComponent(service);
				service.addClickListener(new NavigationButtonClickListener() {

					@Override
					public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {     
						if (nav instanceof SmartphoneMainView){
							((SmartphoneMainView) nav).setService(lw, null);
						}
					}
					
				} );
			}
			else if (s instanceof EventWebsite){ 
				final EventWebsite ew = (EventWebsite)s;
				NavigationButton service = new NavigationButton(ew.getTitle());
				service.setIcon(new ThemeResource("../base/img/Actions-help-hint-icon.png"));
				servicesGroup.addComponent(service);
				service.addClickListener(new NavigationButtonClickListener() {

					@Override
					public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {     
						if (nav instanceof SmartphoneMainView){
							((SmartphoneMainView) nav).setService(ew, null);
						}
					}
					
				} );
			}
			else if (s instanceof Website){
				final Website w = (Website)s;
				NavigationButton service = new NavigationButton(w.getTitle());
				service.setIcon(new ThemeResource("../base/img/Categories-preferences-desktop-personal-icon.png"));
				servicesGroup.addComponent(service);
				service.addClickListener(new NavigationButtonClickListener() {

					@Override
					public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {     
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
