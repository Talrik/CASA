package de.lehsten.casa.mobile.gui;

import com.vaadin.addon.touchkit.ui.TouchKitApplication;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.lehsten.casa.mobile.communication.MobileRouteBuilder;
import de.lehsten.casa.mobile.gui.ui.MainTabsheet;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class CASAMobileApplication extends TouchKitApplication
{
	/**
	 * Make application reload itself when the session has expired. 
	 *
	 * @see TouchKitApplication#getSystemMessages()
	 */
	public static SystemMessages getSystemMessages() {
		return customizedSystemMessages;
	}

	static CustomizedSystemMessages customizedSystemMessages = new CustomizedSystemMessages();
	static {
		customizedSystemMessages.setSessionExpiredNotificationEnabled(false);
	}

	private TouchKitWindow mainWindow;
	
	//RIGZ coordinates
	
	private double currentLatitude =  54.0748861;
	private double currentLongitude = 12.1161766;

	@Override
	public void init(){
		new MobileRouteBuilder(this);
		configureMainWindow();
	}
	
	@Override
	public void onBrowserDetailsReady() {
//		mainWindow.setContent(new MainTabsheet());
		
	}

	private void configureMainWindow() {
		mainWindow = new CASAWindow();
		setMainWindow(mainWindow);
	}



}
