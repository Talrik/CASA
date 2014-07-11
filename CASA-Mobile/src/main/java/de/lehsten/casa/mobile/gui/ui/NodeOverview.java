package de.lehsten.casa.mobile.gui.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout; 
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import de.lehsten.casa.mobile.communication.NodeHandler;
import de.lehsten.casa.mobile.data.Node;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;
import de.lehsten.casa.mobile.gui.CASAUI;

public class NodeOverview extends NavigationView {
	
	HashMap<Switch,Node> switch2node = new HashMap<Switch,Node>();
	CASAUI app ;
	@Override
	public void attach() {
		super.attach();
		buildView();
	}
	
	private void buildView() { 
	
		CssLayout content = new CssLayout();
		content.setWidth("100%");
		this.setCaption("CASA Node Settings"); 

		NodeHandler nodeHandler = CASAUI.getApp().getNodeHandler();
		ArrayList<Node> nodes = nodeHandler.getNodes();
		for(Node n : nodes){
			VerticalComponentGroup nodeSettings = new VerticalComponentGroup();
			nodeSettings.setCaption(n.getDomainType());
			nodeSettings.addComponent(new Label(n.getDescription())); 
			Switch serverConnected = new Switch();
			serverConnected.setCaption("Connected to CASA Node");
			serverConnected.setValue( CASAUI.getApp().getRouteBuilder().isConnected(n));
			serverConnected.setImmediate(true);
			switch2node.put(serverConnected, n);
			serverConnected.addValueChangeListener(new Property.ValueChangeListener(){
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					if (event.getProperty() instanceof Switch) {
						Node node = switch2node.get(event.getProperty());
						System.out.println(node);
						if (((Boolean)((Switch)event.getProperty()).getValue()) == true){
							 CASAUI.getApp().getRouteBuilder().connectToServer(node);
							 CASAUI.getApp().getServiceHandler().refresh();
						}else{
							 CASAUI.getApp().getRouteBuilder().disconnectFromServer(node);
						}
					}
				}
			});
			nodeSettings.addComponent(serverConnected);


			//		privateSettings.addComponent(new TextField("Username"));

			Switch switch1 = new Switch("Use my location");
			switch1.setValue(true);
			switch1.setImmediate(true);
			switch1.addValueChangeListener(new Property.ValueChangeListener(){
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					if (event.getProperty() instanceof Switch) {
						System.out.println(event.getProperty());
						if (((Boolean)((Switch)event.getProperty()).getValue()) == true){
							CASAUI.getApp().refreshLocation();
							CASAUI.getApp().getNodeHandler().updateNode(n, n);
						}else{
							
						}
					}
				}
			});
			nodeSettings.addComponent(switch1);
			NavigationButton b = new NavigationButton("Configure");
			b.setDescription("Configure this Node");
			//        b.addListener(this); // react to clicks
			nodeSettings.addComponent(b);

			content.addComponent(nodeSettings);
		}
		
//--------//		
		/*
		VerticalComponentGroup groupSettings = new VerticalComponentGroup();
		groupSettings.setCaption("Group Node");
		groupSettings.addComponent(new Label("StudIP Node @ elbe5"));
		serverConnected = new Switch();
		serverConnected.setCaption("Connected to CASA Node");
		serverConnected.setValue(false);
		serverConnected.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("Value changed");
				
			}
		});
		groupSettings.addComponent(serverConnected);
		
		groupSettings.addComponent(new TextField("Username"));
		
		switch1 = new Switch("Use my location");
		switch1.setValue(true);
		groupSettings.addComponent(switch1);		
		b = new NavigationButton("Configure");
        b.setDescription("Configure this Node");
//      b.addListener(this); // react to clicks
        groupSettings.addComponent(b);
		
		content.addComponent(groupSettings);
//--------//		
		VerticalComponentGroup publicSettings = new VerticalComponentGroup();
		publicSettings.setCaption("Public Node");
		publicSettings.addComponent(new Label("OpenStreetMaps Node @ elbe5"));
		serverConnected = new Switch();
		serverConnected.setCaption("Connected to CASA Node");
		serverConnected.setValue(false);
		serverConnected.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("Value changed");
			}
		});
		publicSettings.addComponent(serverConnected);
		
		switch1 = new Switch("Use my location");
		switch1.setValue(true);
		publicSettings.addComponent(switch1);
        
		b = new NavigationButton("Configure");
        b.setDescription("Configure this Node");
//        b.addListener(this); // react to clicks
        publicSettings.addComponent(b);
		
		content.addComponent(publicSettings);
		
*/
		setContent(content);
	}

	

}
