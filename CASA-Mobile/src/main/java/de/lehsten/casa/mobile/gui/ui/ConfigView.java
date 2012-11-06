package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import org.vaadin.vol.*;

public class ConfigView extends NavigationView{
	
	OpenLayersMap map;
	
	public ConfigView(){
		this.setCaption("Settings");
		VerticalComponentGroup settings = new VerticalComponentGroup();
/*		settings.addComponent(new Label("Server Status"));
		Switch serverConnected = new Switch();
		serverConnected.setCaption("Connected to CASA");
		serverConnected.setValue(false);
		serverConnected.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("Value changed");
				
			}
		});
		settings.addComponent(serverConnected);
*/		this.setContent(settings);
	}

}
