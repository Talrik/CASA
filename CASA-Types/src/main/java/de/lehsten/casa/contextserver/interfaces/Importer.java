package de.lehsten.casa.contextserver.interfaces;

import java.util.HashMap;

public interface Importer {
	
	public String getTitle(); 
	public String getDescription();
	public String getDeveloper();
	public String getRequiredVersionOfCASATypes();
	
	public boolean startImport(ImporterManager mgr);
	public boolean startImport(ImporterManager mgr, HashMap<String,String> parameter); 
	public boolean startImport(ImporterManager mgr, Object... parameter); 
	public boolean stopImport(); 
	public boolean updateImports(ImporterManager mgr);

}
