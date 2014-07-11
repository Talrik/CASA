package de.lehsten.casa.mobile.gui;

import java.io.IOException;

import javax.jmdns.JmDNS;

import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import de.lehsten.casa.mobile.communication.MobileRouteBuilder;
import de.lehsten.casa.mobile.communication.NodeHandler;
import de.lehsten.casa.mobile.data.ServiceHandler;
import de.lehsten.casa.mobile.gui.ui.MainTabsheet;

@Theme("touchkit")
public class CASAUI extends UI{
	
	private JmDNS jmDNS;
	private MobileRouteBuilder mrb;
	
	//RIGZ coordinates	
	private double currentLatitude =  54.0748861;
	private double currentLongitude = 12.1161766;
	private double accuracy = 1;

	private ServiceHandler serviceHandler;
	private NodeHandler nodeHandler;

	@Override
	public void init(VaadinRequest request) {
		mrb = new MobileRouteBuilder(this);
		this.nodeHandler = new NodeHandler(this);
		try {
			String serviceType = "_casa._tcp.local.";
			jmDNS = JmDNS.create();
			jmDNS.addServiceListener(serviceType, this.nodeHandler);
		}catch(IOException e){ 
		
		}
		this.serviceHandler = new ServiceHandler(this);
		
		setContent(new MainTabsheet());
	}
	
	public void refreshLocation(){
		Geolocator.detect(new PositionCallback() {
		    public void onSuccess(Position position) {
		        currentLatitude  = position.getLatitude();
		        currentLongitude = position.getLongitude();
		        accuracy  = position.getAccuracy();
		    }

		    public void onFailure(int errorCode) {
		    	 switch(errorCode){ 
		    	 case 0:
		    		 System.err.println("Unknown error");
		    		 break;
		    	 case 1:
		    		 System.err.println("Permission denied");
		    		 break;
		    	 case 2:
		    		 System.err.println("Position is unavailable");
		    		 break;
		    	 case 3:
		    		 System.err.println("Positioning timedout");
		    		 break;
		    	 }
		    }
		});
	}
	
	public double getCurrentLongitude() {
		return currentLongitude;
	}
	
	public void setCurrentLongitude(double currentLongitude) {
		this.currentLongitude = currentLongitude;
    }
	
	public double getCurrentLatitude() {
		return currentLatitude;
	}
	
	public void setCurrentLatitude(double currentLatitude) {
		this.currentLatitude = currentLatitude;
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
	
	public static CASAUI getApp(){
		return (CASAUI) UI.getCurrent();
	}


}
