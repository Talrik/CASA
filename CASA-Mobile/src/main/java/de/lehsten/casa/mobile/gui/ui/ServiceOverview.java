package de.lehsten.casa.mobile.gui.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationBar;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.mobile.data.ServiceContainer;
import de.lehsten.casa.mobile.data.ServiceHandler;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;
import de.lehsten.casa.mobile.gui.CASAUI;

public class ServiceOverview extends NavigationView{
	
    private static final long serialVersionUID = 1L;
    
    private ServiceContainer sc =  CASAUI.getApp().getServiceHandler().getContainer();
    private ServiceContainer locationWebsites;
    final ServiceHandler sh =  CASAUI.getApp().getServiceHandler();
    final MainNavigationManager nav;
    
	public ServiceOverview(final MainNavigationManager nav){
		
        setCaption("Services");
        setWidth("100%");
        setHeight("100%");
        this.nav = nav;
		createServiceOverview();
		
	}
	

    static Component createToolbar() {

        final NavigationBar toolbar = new NavigationBar();

        Button refresh = new Button();
 //       refresh.setIcon(refreshIcon);

        toolbar.setLeftComponent(refresh);

        final SimpleDateFormat formatter = new SimpleDateFormat(
                "M/d/yy hh:mm");
        toolbar.setCaption("Updated "
                + formatter.format(Calendar.getInstance().getTime()));

        refresh.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                toolbar.setCaption("Updated "
                        + formatter.format(Calendar.getInstance().getTime()));
            }
        });
/*
        TouchKitApplication touchKitApplication = CASAMobileApplication.get();
        if (touchKitApplication instanceof CASAMobileApplication) {
        	CASAMobileApplication app = (CASAMobileApplication) touchKitApplication;
            if (app.isSmallScreenDevice()) {
                ClickListener showComposeview = new ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        Window window = event.getButton().getWindow();
  //                      window.addWindow(new ComposeView(true));
                    }
                };
                Button button = new Button(null, showComposeview);
 //               button.setIcon(new ThemeResource("graphics/compose-icon.png"));
                toolbar.setRightComponent(button);
 //               button.addStyleName("no-decoration");
            }
        }
*/
        return toolbar;
    }
    
    private void createServiceOverview(){
    	VerticalComponentGroup serviceTypes = new VerticalComponentGroup();
		sh.refresh();
		NavigationButton service2 = new NavigationButton("Location Services");
		locationWebsites = sh.getContainerLocationWebsites(); 
		service2.setDescription(locationWebsites.size() +"");
		service2.addClickListener(new NavigationButtonClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {      
                ServiceTypeOverview v = new ServiceTypeOverview(nav, locationWebsites);
                nav.navigateTo(v);
            }
        });
		service2.setIcon(new ThemeResource("../base/img/maps-icon.png"));
		serviceTypes.addComponent(service2);
		
		NavigationButton service3 = new NavigationButton("Event Services");
		final ServiceContainer eventWebsites = sh.getContainerEventWebsites(); 
		service3.setDescription(eventWebsites.size() +"");
		service3.addClickListener(new NavigationButtonClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {          
                ServiceTypeOverview v = new ServiceTypeOverview(nav, eventWebsites);
                nav.navigateTo(v);
            }
        });
		
		service3.setIcon(new ThemeResource("../base/img/Actions-help-hint-icon.png"));
		serviceTypes.addComponent(service3);
		
		NavigationButton service4 = new NavigationButton("Personal Services");
		final ServiceContainer personalWebsites = sh.getContainerPersonalWebsites(); 
		service4.setDescription(personalWebsites.size() +"");
		service4.addClickListener(new NavigationButtonClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {     
                ServiceTypeOverview v = new ServiceTypeOverview(nav, personalWebsites);
                nav.navigateTo(v);
            }
        });
		service4.setIcon(new ThemeResource("../base/img/Categories-preferences-desktop-personal-icon.png"));
		serviceTypes.addComponent(service4);
		
		
		
		
		this.setContent(serviceTypes);
		setToolbar(createToolbar());
    	
    }
    
    public void onBecomingVisible(){
		createServiceOverview();
    }

}
