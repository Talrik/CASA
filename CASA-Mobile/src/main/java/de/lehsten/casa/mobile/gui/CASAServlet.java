package de.lehsten.casa.mobile.gui;

import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinService;



@VaadinServletConfiguration(widgetset="de.lehsten.casa.mobile.gui.gwt.AppWidgetSet",productionMode = false, ui = CASAUI.class)
public class CASAServlet extends TouchKitServlet {

	private CASAUIProvider uiProvider = new CASAUIProvider();

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();	       
		getService().addSessionInitListener(new SessionInitListener() {
			@Override
			public void sessionInit(SessionInitEvent event) throws ServiceException {
				event.getSession().addUIProvider(uiProvider);
			}
		});
		
		TouchKitSettings s = getTouchKitSettings();
        s.getWebAppSettings().setWebAppCapable(true);
		s.getWebAppSettings().setStatusBarStyle("black");
		String contextPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		s.getApplicationIcons().addApplicationIcon(contextPath + "/base/img/crystal-clear-App-browser-icon.png");
		s.getWebAppSettings().setStartupImage(contextPath + "/base/img/crystal-clear-App-browser-icon.png");
				
		/*
		String contextPath = getServletConfig().getServletContext().getC
		s.getApplicationIcons().addApplicationIcon(
				contextPath + "VAADIN/themes/vornitologist/icon.png");
		s.getWebAppSettings().setStartupImage(
				contextPath + "VAADIN/themes/vornitologist/startup.png");
		 */
		s.getApplicationCacheSettings().setCacheManifestEnabled(true);
		
    }	

	
	

}
