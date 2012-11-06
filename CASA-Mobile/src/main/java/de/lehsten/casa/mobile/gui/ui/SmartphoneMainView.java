package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.terminal.ExternalResource;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;

public class SmartphoneMainView extends MainNavigationManager implements ServiceView{

	@Override
	public void setService(Service service, ServiceOverview serviceOverview) {
		if (service instanceof Website){
			CASAMobileApplication.getApp().getMainWindow().open(new ExternalResource(((Website) service).getTargetURL()));
		}
	}

}
