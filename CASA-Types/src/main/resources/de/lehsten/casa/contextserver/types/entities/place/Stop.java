package de.lehsten.casa.contextserver.types.entities.place;

import java.util.ArrayList;

public class Stop extends Place{
	
	int StopNumber;	
	ArrayList<String> lines = new ArrayList<String>();

	public Double getLongitude() {
		return this.getLongitude();
	}
	public Double getLatitude() {
		return this.getLatitude();
	}
	
	public int getStopNumber() {
		return StopNumber;
	}

	public void setStopNumber(int stopNumber) {
		StopNumber = stopNumber;
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
