package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.extensions.Geolocator;
import com.vaadin.addon.touchkit.extensions.PositionCallback;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import de.lehsten.casa.mobile.data.Node;
import de.lehsten.casa.mobile.gui.CASAUI;


public class ConfigView extends NavigationView{
	
	
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
		username.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					System.out.println(event.getProperty().getValue());
				}

			}
		});
		settings.addComponent(username);

		Switch switch1 = new Switch("Use my location");
		switch1.setValue(true);
		switch1.addValueChangeListener(new Property.ValueChangeListener(){
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if (event.getProperty() instanceof Switch) {
					if (((Boolean)((Switch)event.getProperty()).getValue()) == true){
						Geolocator.detect(new PositionCallback() {
						    public void onSuccess(Position position) {
						        double latitude  = position.getLatitude();
						        double longitude = position.getLongitude();
						        double accuracy  = position.getAccuracy();
						        System.out.println("New Location: Lat:"+latitude+" Lon:"+longitude+" Acc:"+accuracy);
						    }

						    public void onFailure(int errorCode) {
						    	System.out.println("Error"+errorCode);
						    }
						});
					}else{
					}
				}
			}
		});
		settings.addComponent(switch1);

		NavigationButton nodeConfig = new NavigationButton("Configure Nodes");
		nodeConfig.addClickListener(new NavigationButton.NavigationButtonClickListener (){

			@Override
			public void buttonClick(NavigationButtonClickEvent event) {
				getNavigationManager().navigateTo(new NodeOverview());
			}
			
		} );
		settings.addComponent(nodeConfig);

		content.addComponent(settings);
		setContent(content);
	}

}
