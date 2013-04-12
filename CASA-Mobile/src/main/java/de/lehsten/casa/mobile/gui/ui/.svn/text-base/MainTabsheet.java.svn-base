package de.lehsten.casa.mobile.gui.ui;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.TabSheet.Tab;

public class MainTabsheet extends TabBarView{

	public MainTabsheet(){
		Tab serviceTab = addTab(new ServiceNavigationManager(),"Services");
		serviceTab.setCaption("Services");
		serviceTab.setIcon(new ThemeResource("img/crystal-clear-App-browser-icon.png"));
		Tab configTab = addTab(new ConfigNavigationManager());
		configTab.setCaption("Config");
		configTab.setIcon(new ThemeResource("img/Oxygen-Apps-esd-icon.png"));
		this.setSelectedTab(serviceTab);
		
//        setSelectedTab(services);
		
	}
}
