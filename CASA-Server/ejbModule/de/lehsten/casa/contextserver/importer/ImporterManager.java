package de.lehsten.casa.contextserver.importer;

import java.util.ArrayList;

public interface ImporterManager {
	
	public ArrayList<String> getImporterNames();
	public String getQueue();
	public boolean startImporter(String name);
	public boolean stopImporter(String name);

}
