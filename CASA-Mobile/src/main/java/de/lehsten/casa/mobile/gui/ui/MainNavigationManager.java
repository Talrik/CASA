package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationManager;

import de.lehsten.casa.mobile.gui.CASAMobileApplication;

public class MainNavigationManager extends NavigationManager{
	
	private static final long serialVersionUID = 1L;
	
	public MainNavigationManager(){
//		setWidth("300px");
		navigateTo(new ServiceOverview(this));
	}

}
