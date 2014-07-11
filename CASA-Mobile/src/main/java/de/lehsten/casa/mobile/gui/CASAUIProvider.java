package de.lehsten.casa.mobile.gui;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class CASAUIProvider extends UIProvider{
	
    @Override
	    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
	        String userAgent = event.getRequest().getHeader("user-agent").toLowerCase();
	        if(userAgent.contains("webkit")) {
	            return CASAUI.class;
	        } else {
	        	return CASAFallbackUI.class;
	            //return CASAFallbackUI.class;
	        }
	    }
	
	}