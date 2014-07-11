package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.server.ExternalResource;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;
import de.lehsten.casa.mobile.gui.CASAUI;

public class SmartphoneMainView extends MainNavigationManager implements ServiceView{

	@Override
	public void setService(Service service, ServiceOverview serviceOverview) {
		if (service instanceof Website){
//			CASAUI.getApp().get .getMainWindow().open(new ExternalResource(((Website) service).getTargetURL()));
		}
	}

}
