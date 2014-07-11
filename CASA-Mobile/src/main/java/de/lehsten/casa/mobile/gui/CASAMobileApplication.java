package de.lehsten.casa.mobile.gui;

import java.io.IOException;

import javax.jmdns.JmDNS;

import com.vaadin.addon.touchkit.ui.TouchKitApplication;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.lehsten.casa.mobile.communication.NodeHandler;
import de.lehsten.casa.mobile.communication.MobileRouteBuilder;
import de.lehsten.casa.mobile.data.ServiceHandler;
import de.lehsten.casa.mobile.gui.ui.MainTabsheet;
import de.lehsten.casa.mobile.gui.ui.SmartphoneMainView;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class CASAMobileApplication extends TouchKitApplication
{

	private TouchKitWindow mainWindow;
	private JmDNS jmDNS;
	private MobileRouteBuilder mrb;
	
	//RIGZ coordinates	
	private double currentLatitude =  54.0748861;
	private double currentLongitude = 12.1161766;

	private ServiceHandler serviceHandler;
	private NodeHandler nodeHandler;
	
	public static SystemMessages getSystemMessages() {
	    CustomizedSystemMessages messages =
	            new CustomizedSystemMessages();
	    messages.setSessionExpiredCaption("Ohno, session expired!");
	    messages.setSessionExpiredMessage("Don't idle!");
	    messages.setSessionExpiredNotificationEnabled(true);
	    messages.setSessionExpiredURL("http://vaadin.com/");
	    return messages;
	}

	@Override
	public void init(){
		mrb = new MobileRouteBuilder(this);
		this.nodeHandler = new NodeHandler(this);
		try {
			String serviceType = "_casa._tcp.local.";
			jmDNS = JmDNS.create();
			jmDNS.addServiceListener(serviceType, this.nodeHandler);
		}catch(IOException e){ 
		
		}
		this.serviceHandler = new ServiceHandler(this);
		
		configureMainWindow();
	}
	
	@Override
	public void onBrowserDetailsReady() {
        WebBrowser browser = getBrowser();
/*        if (!browser.isTouchDevice()) {
            getMainWindow()
                    .showNotification(
                            "You appear to be running on a desktop software or other non touch device. We'll show you the tablet (or smartphone view if small screen size) for debug purposess.");
        }
*/
        if (isSmallScreenDevice() || true) {
            getMainWindow().setContent(new MainTabsheet());
        } else {
 //           getMainWindow().setContent(new TabletMainView());
        }		
	}

	private void configureMainWindow() {
		mainWindow = new CASAWindow();
		setMainWindow(mainWindow);
	}
	
	public ServiceHandler getServiceHandler(){
		return this.serviceHandler;
	}

	public NodeHandler getNodeHandler(){
		return this.nodeHandler;
	}
	
	public MobileRouteBuilder getRouteBuilder(){
		return mrb;
	}
	
	public static CASAMobileApplication getApp(){
		return (CASAMobileApplication) get();
	}
	
    public boolean isSmallScreenDevice() {
        /*float viewPortWidth = getMainWindow().getWidth();
        return viewPortWidth < 600;
        */
    	return true;
    }

}
