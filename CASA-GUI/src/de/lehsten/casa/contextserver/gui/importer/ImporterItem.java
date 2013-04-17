package de.lehsten.casa.contextserver.gui.importer;


public class ImporterItem{
	String name = "";
	
	public ImporterItem (String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}