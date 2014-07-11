package de.lehsten.casa.contextserver.types.entities.place;

public class Cafeteria extends Building{
	
	public String lactosefree_menu;
	public String menu;
	public boolean lactosefree_menu_available = true; 
	
	
	public boolean isLactosefree_menu_available() {
		return lactosefree_menu_available;
	}

	public void setLactosefree_menu_available(boolean lactosefree_menu_available) {
		this.lactosefree_menu_available = lactosefree_menu_available;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getLactosefree_menu() {
		return lactosefree_menu;
	}

	public void setLactosefree_menu(String lactosefree_menu) {
		this.lactosefree_menu = lactosefree_menu;
	}
	

}
