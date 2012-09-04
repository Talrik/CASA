package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;

public class ServiceTypeOverview extends VerticalComponentGroup{
	
	public ServiceTypeOverview(){
		
		NavigationButton service1 = new NavigationButton("Timed Services");
		service1.setIcon(new ThemeResource("img/Actions-chronometer-icon.png"));
		service1.setTargetView(new ServiceOverview());
		this.addComponent(service1);		
		
		NavigationButton service2 = new NavigationButton("Location Services");
		service2.setIcon(new ThemeResource("img/maps-icon.png"));
		this.addComponent(service2);
		
		NavigationButton service3 = new NavigationButton("Event Services");
		service3.setIcon(new ThemeResource("img/Actions-help-hint-icon.png"));
		this.addComponent(service3);
		
		NavigationButton service4 = new NavigationButton("Personal Services");
		service4.setIcon(new ThemeResource("img/Categories-preferences-desktop-personal-icon.png"));
		this.addComponent(service4);
		
		Switch switcher = new Switch();
		switcher.setCaption("Do I look like iOS?");
		this.addComponent(switcher);
		
	}

}
