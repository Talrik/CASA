package de.lehsten.casa.contextserver.types.position;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import de.lehsten.casa.contextserver.types.Context;

public class Position extends Context{
	
	private Double latitude; 
	private Double longitude;
	
	public Position(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position() {
	}
	
	/**
	 * @param location - designation / description of the location
	 * @return {@link Position} containing geographic coordinates
	 */
	public Position getPositionFromString(String location){
		Position position = null;
		double lat, lon;
		
		// Koordinaten mittels Google Maps (XML) und SAXBuilder auslesen
		String url = "http://maps.google.com/maps/geo?q="+location+"&output=xml&sensor=false";
		Document xml;
		try {
			xml = new SAXBuilder().build(url);
			Element root = xml.getRootElement();
			Namespace xmlNamespace = root.getNamespace();
			Element coordinatesElement = root.getChild("Response", xmlNamespace).getChild("Placemark", xmlNamespace).getChild("Point", xmlNamespace).getChild("coordinates", xmlNamespace);
			String coordinates = coordinatesElement.getText();

			// Koordinaten übernehmen
			lon = Double.valueOf(coordinates.substring(0,9));
			lat = (Double.valueOf(coordinates.substring(11,20)));
			position = new Position(lat, lon);
			
		} catch (JDOMException e) {
			System.out.println("JDOMException : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
	
		return position;
	}
	
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLon(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return getLatitude() + "," + getLongitude();
	}
	
}
