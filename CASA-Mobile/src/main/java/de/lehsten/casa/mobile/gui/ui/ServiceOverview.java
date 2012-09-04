package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;

public class ServiceOverview extends NavigationView{
	
	public ServiceOverview(){
		
		VerticalComponentGroup services = new VerticalComponentGroup();
		NavigationButton service1 = new NavigationButton("Service 1");
		service1.setIcon(new ThemeResource("img/Actions-chronometer-icon.png"));
		services.addComponent(service1);		
		
		NavigationButton service2 = new NavigationButton("Service 2");
		service2.setIcon(new ThemeResource("img/maps-icon.png"));
		services.addComponent(service2);
		
		NavigationButton service3 = new NavigationButton("Service 3");
		service3.setIcon(new ThemeResource("img/Actions-help-hint-icon.png"));
		services.addComponent(service3);
		
		NavigationButton service4 = new NavigationButton("Service 4");
		service4.setIcon(new ThemeResource("img/Categories-preferences-desktop-personal-icon.png"));
		services.addComponent(service4);
		
		this.setContent(services);
	}

}
