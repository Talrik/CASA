package de.lehsten.casa.contextserver.types.entities.place;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Stop extends Place{
	
	private int stopNumber;	
	ArrayList<String> lines = new ArrayList<String>();
	
	public int getStopNumber() {
		return stopNumber;
	}

	public void setStopNumber(int stopNumber) {
		this.stopNumber = stopNumber;
	} 

	public ArrayList<String> getLines() {
		return lines;
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}
	
	public void addLines(String l) {
		this.lines.add(l);
	}

}
