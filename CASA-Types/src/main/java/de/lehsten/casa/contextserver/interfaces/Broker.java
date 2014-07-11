package de.lehsten.casa.contextserver.interfaces;

import java.util.Collection;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Request;
import de.lehsten.casa.contextserver.types.entities.services.Service;

/**
 * @author phil
 *
 */
public interface Broker {
	
	/**
	 * @param request which specifies the restrictions 
	 * @return Array with all Service objects matching the provide Request
	 */
	public Service[] getServices(Request request);
	/**
	 * @param service which is to be set 
	 * @param restrictions which have to be met by a request
	 * @return
	 */
	public Service setService(Service service, Entity[] restrictions);
	/**
	 * @param service which is to be removed
	 * @return null if the removal was not successful or the service was not found, otherwise an empty array if the was removed
	 */
	public Service[] removeService(Service service);

	/**
	 * @param oldService which shall be replaces
	 * @param newService which is the one to replace the old one
	 * @return 
	 */
	public Service updateService(Service oldService, Service newService);

}
