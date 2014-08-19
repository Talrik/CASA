package de.lehsten.casa.mobile.gui.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.mobile.communication.NodeHandler;
import de.lehsten.casa.mobile.data.Node;
import de.lehsten.casa.mobile.gui.CASAUI;

public class NodeConfig extends NavigationView {
	
	@Override
	public void attach() {
		super.attach();
		buildView();
	}
	
	private void buildView() { 
	
		CssLayout content = new CssLayout();
		content.setWidth("100%");
		this.setCaption("CASA Node Configuration"); 

		NodeHandler nodeHandler = CASAUI.getApp().getNodeHandler();
		ArrayList<Node> nodes = nodeHandler.getNodes();
		for(Node n : nodes){
			content.addComponent(buildNodeConfig(n));
		}

		setContent(content);
		
}
	
	private VerticalComponentGroup buildNodeConfig(final Node n){
		VerticalComponentGroup nodeConfig = new VerticalComponentGroup();
		VerticalComponentGroup activeQueries = new VerticalComponentGroup();
		activeQueries.addComponent(new Label("Active Queries"));
		for(final String s : n.getActiveQueries().keySet()){
			
			NavigationButton b = new NavigationButton(s);
			b.addClickListener(new NavigationButton.NavigationButtonClickListener (){

				@Override
				public void buttonClick(NavigationButtonClickEvent event) {
					getNavigationManager().navigateTo(new NodeConfig());
				}
				
			} );
			b.setDescription("Configure the query "+s);
			
			activeQueries.addComponent(b);
		}
		
		VerticalComponentGroup availableQueries = new VerticalComponentGroup();
		availableQueries.addComponent(new Label("Available Queries"));
		for(final String s : n.getAvailableQueries()){
			Switch sw = new Switch(s);
			sw.setValue(n.getActiveQueries().containsKey(sw));
			sw.addValueChangeListener(new Property.ValueChangeListener(){
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					if (event.getProperty() instanceof Switch) {
						if (((Boolean)((Switch)event.getProperty()).getValue()) == true){
							n.getActiveQueries().put(s, new HashMap<String,Object>());
						}
						else{
							
						}
					}else{
							
					}
				}
				
			});
			availableQueries.addComponent(sw);
		}
		nodeConfig.addComponent(activeQueries);
		nodeConfig.addComponent(availableQueries);
		return nodeConfig;
	}
}
