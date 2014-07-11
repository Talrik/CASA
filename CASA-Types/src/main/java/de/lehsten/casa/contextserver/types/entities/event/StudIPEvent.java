package de.lehsten.casa.contextserver.types.entities.event;

public class StudIPEvent extends Event{
	

	private String StudIP_ID;
	private boolean isAssigned = false;
	
	public StudIPEvent(){
		super();
	}
	
	public StudIPEvent(String title, Long begin, Long end, String StudIP_ID){
		this.setTitle(title);
		this.setBeginDate(begin);
		this.setEndDate(end);
		this.setStudIP_ID(StudIP_ID);
		
	}
	
	public String getStudIP_ID() {
		return this.StudIP_ID;
	}

	public void setStudIP_ID(String studIP_ID) {
		this.StudIP_ID = studIP_ID;
		this.getProperties().put("StudIP_ID", studIP_ID);
	}
	
	public boolean getisAssigned() {
		return isAssigned;
	}

	public void setisAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

}
