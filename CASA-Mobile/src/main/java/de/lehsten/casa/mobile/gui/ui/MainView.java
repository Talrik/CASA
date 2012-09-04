package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Label;

public class MainView extends NavigationView{
	
	public MainView(){
		this.setCaption("Main View");
		
		VerticalComponentGroup services = new VerticalComponentGroup();
		VerticalComponentGroup serviceTypes = new ServiceTypeOverview();
		
		services.addComponent(serviceTypes);
		services.addComponent(serviceTypes);
		this.setContent(services);
	}

}
