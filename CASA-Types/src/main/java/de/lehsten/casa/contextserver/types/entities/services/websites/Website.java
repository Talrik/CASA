package de.lehsten.casa.contextserver.types.entities.services.websites;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.entities.services.Service;

@XmlRootElement
public class Website extends Service{
	
	private URL targetURL;

	public URL getTargetURL() {
		return targetURL;
	}
	
	public void setTargetURL(URL targetURL) {
		this.targetURL = targetURL;
		this.getProperties().put("targetURL", targetURL.toString());
	}

	public void setTargetURL(String targetURL) {
		try {
			this.targetURL = new URL(targetURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getProperties().put("targetURL", targetURL);
	}

}
