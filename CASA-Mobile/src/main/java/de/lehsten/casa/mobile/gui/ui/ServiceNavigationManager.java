package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;


public class ServiceNavigationManager extends SmartphoneMainView{
	
	private static final long serialVersionUID = 1L;
	
	public ServiceNavigationManager(){
		navigateTo(new ServiceOverview(this));
	}	


}
