package de.lehsten.casa.contextserver.types.entities.services.websites;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.media.Media;

@XmlRootElement
public class MediaWebsite extends Website {

	private Media media; 
	private boolean embeddable;
	
	public boolean isEmbeddable() {
		return embeddable;
	}
	public void setEmbeddable(boolean embeddable) {
		this.embeddable = embeddable;
	}
	public Media getMedia() {
		return media;
	}
	public void setMedia(Media media) {
		this.media = media;
	}
	
}
