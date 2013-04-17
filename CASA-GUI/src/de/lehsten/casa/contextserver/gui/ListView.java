package de.lehsten.casa.contextserver.gui;

import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalSplitPanel;


public class ListView extends VerticalSplitPanel {
	
	public ListView(){}
	
    public ListView(GenericList genericList, Component component) {
        setFirstComponent(genericList);
        setSecondComponent(component);
        setSplitPosition(40);
    }
    
    public void setUpperComponent(Component c){
    	setFirstComponent(c);
    }
    
    public void setLowerComponent(Component c){
    	setSecondComponent(c);
    }
}