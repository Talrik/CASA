package de.lehsten.casa.contextserver.types.media;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.Entity;

public class Media extends Entity {
	
	private String title;
	private String mediaType;
	private String licence;
	private String author;
	private ArrayList<String> connected = new ArrayList<String>();
	private URL path; 
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		this.getProperties().put("title", title);
	}
	
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
		this.addProperty("mediaType", mediaType);
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
		this.addProperty("licence", licence);
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
		this.addProperty("author", author);
	}
	public ArrayList<String> getConnected() {
		return connected;
	}
	public void setConnected(ArrayList<String> connected) {
		this.connected = connected;
	}
	public void addConnection(String  id) {
		
		this.connected.add(id);
	}
	public void removeConnection(String id) {
		this.connected.remove(id);
	}
	public URL getPath() {
		return path;
	}
	public void setPath(URL path) {
		this.path = path;
		this.addProperty("path", path.toString());
	}
	
	public void setPath(String path) {
		try {
			this.path = new URL(path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getProperties().put("path", path);
	}

}
