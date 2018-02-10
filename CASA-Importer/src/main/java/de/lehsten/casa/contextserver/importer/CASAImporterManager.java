package de.lehsten.casa.contextserver.importer;

import java.util.ArrayList;

import de.lehsten.casa.contextserver.importer.studip.StudIPImporter;

public class CASAImporterManager implements ImporterManager{

	@Override
	public ArrayList<String> getImporterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean startImporter(String name) {
		// TODO Auto-generated method stub
		StudIPImporter studip = new StudIPImporter(this);
		studip.startImport(this);
		return true;
	}

	@Override
	public boolean stopImporter(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return "jms.queue.FactEntry";
	}

}
