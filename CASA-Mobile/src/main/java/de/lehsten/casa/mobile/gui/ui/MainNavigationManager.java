package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationManager;

public class MainNavigationManager extends NavigationManager{
	
	public MainNavigationManager(){
		this.setCurrentComponent(new MainView());
		this.setPreviousComponent(new MainView());
		
	}

}
