package de.lehsten.casa.mobile.gui.ui;

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

public class NodeOverview extends NavigationView{
	

	@Override
	public void attach() {
		super.attach();
		buildView();
	}
	
	private void buildView() {
	
		CssLayout content = new CssLayout();
		content.setWidth("100%");
		
		this.setCaption("CASA Node Settings");
		VerticalComponentGroup privateSettings = new VerticalComponentGroup();
		privateSettings.setCaption("Private Node");
		privateSettings.addComponent(new Label("Phils Node @ elbe5"));
		Switch serverConnected = new Switch();
		serverConnected.setCaption("Connected to CASA Node");
		serverConnected.setValue(false);
		serverConnected.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("Value changed");
				
			}
		});
		privateSettings.addComponent(serverConnected);
		
		privateSettings.addComponent(new TextField("Username"));
		
		Switch switch1 = new Switch("Use my location");
		switch1.setValue(true);
		privateSettings.addComponent(switch1);
		NavigationButton b = new NavigationButton("Configure");
        b.setDescription("Configure this Node");
//        b.addListener(this); // react to clicks
        privateSettings.addComponent(b);
		
		content.addComponent(privateSettings);
//--------//		
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
		
		
		setContent(content);
	}
	

}
