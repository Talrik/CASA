package de.lehsten.casa.contextserver.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import de.lehsten.casa.utilities.interfaces.Messenger;


public interface ImporterManager {

	public String[] getImporterNames();
	/**
	 * @param name of the requesting module
	 * @return Messenger which is configured 
	 */
	
	/**
	 * @return Array with all currently installed Importer objects
	 */
	public Importer[] getImporter();
	/**
	 * @return Array with all currently installed ImporterManager objects
	 */
	public ImporterManager[] getImporterManager();
	
	public Messenger getMessenger(String name);
	
	/**
	 * @param name of the Importer to start
	 * @return true if start was successful, false if not
	 */
	public boolean startImporter(String name);
	public boolean startImporter(String name, HashMap<String,String> properties);
	public boolean stopImporter(String name);
	public boolean installImporter(String name, Importer importer);
	public boolean uninstallImporter(String name);
	
	public boolean installImporterManager(String name, ImporterManager importerMgr);
	public boolean uninstallImporterManager(String name);

}
