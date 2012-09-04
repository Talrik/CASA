package de.lehsten.casa.mobile.gui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Label;

import de.lehsten.casa.mobile.gui.ui.MainNavigationManager;
import de.lehsten.casa.mobile.gui.ui.MainTabsheet;
import de.lehsten.casa.mobile.gui.ui.MainView;

public class CASAWindow extends TouchKitWindow{
	
	public CASAWindow(){
		setCaption("CASA Mobile Application");
		setWebAppCapable(true);
		setPersistentSessionCookie(true);
		
		this.setContent(new MainTabsheet());
//		this.addComponent(services);
//		this.addComponent(new MainTabsheet());
		
	}

}
