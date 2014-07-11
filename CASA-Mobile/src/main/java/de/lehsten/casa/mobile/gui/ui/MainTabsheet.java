package de.lehsten.casa.mobile.gui.ui;

import java.io.File;

import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.TabSheet.Tab;

public class MainTabsheet extends TabBarView{

	public MainTabsheet(){
		
		Tab serviceTab = addTab(new ServiceNavigationManager(),"Services");
		serviceTab.setCaption("Services");
		String contextPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		
		serviceTab.setIcon(new ThemeResource("../base/img/crystal-clear-App-browser-icon.png"));
		Tab configTab = addTab(new ConfigNavigationManager());
		configTab.setCaption("Config");
		configTab.setIcon(new ThemeResource("../base/img/Oxygen-Apps-esd-icon.png"));
		this.setSelectedTab(serviceTab);
		
	}
}
