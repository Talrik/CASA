package services;

import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.utilities.services.ServiceProxy;
import de.lehsten.casa.utilities.services.ServiceProxyImp;

public class Haltestellen extends ServiceProxyImp{
	
	public Haltestellen(){

		this.setQuery("GetCloseStopWebsites");
		Object[] params = new Object[3];
		params[0] = 54.0744279d;
		params[1] = 12.1035669d;
		params[2] = 1d;
		this.setParams(params);
		this.setServieType(LocationWebsite.class);
		this.setTitle("Haltestellen");
		this.setDescription("Haltestellen in einem Umkreis von 1km.");
	}

}
