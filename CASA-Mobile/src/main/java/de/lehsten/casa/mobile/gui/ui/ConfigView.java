package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import org.vaadin.vol.*;

public class ConfigView extends NavigationView{
	
	OpenLayersMap map;
	
	@Override
	public void attach() {
		super.attach();
		buildView();
	}
	
	private void buildView(){
		setCaption("General Settings");

		CssLayout content = new CssLayout();
		content.setWidth("100%");
		
		VerticalComponentGroup settings = new VerticalComponentGroup();
		settings.setCaption("Node Settings");
		settings
				.addComponent(new Label(
						"You are connected since: "));
		TextField username = new TextField("Username");
		username.setDebugId("username");
		username.setWidth("100%");
		username.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					System.out.println(event.getProperty().getValue());
				}

			}
		});
		settings.addComponent(username);

		Switch switch1 = new Switch("Use my location");
		switch1.setValue(true);
		settings.addComponent(switch1);

		NavigationButton nodeConfig = new NavigationButton("Configure Nodes");
		nodeConfig.addListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				((SmartphoneMainView)event.getButton().getParent().getParent().getParent().getParent()).navigateTo(new NodeOverview());
			}
			
		} );
		settings.addComponent(nodeConfig);

		content.addComponent(settings);
		setContent(content);
	}

}
