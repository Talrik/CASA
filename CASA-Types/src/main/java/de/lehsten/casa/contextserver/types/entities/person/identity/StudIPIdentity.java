package de.lehsten.casa.contextserver.types.entities.person.identity;

public class StudIPIdentity extends Identity{
	

	private  String studip_user_id;
	private  String studip_userName;
	//TODO Rollen sind veranstaltungsspezifisch!!!
	private  String studip_role;
		
	public StudIPIdentity(){
		super();
	}
	
	public StudIPIdentity(String StudIP_user_id){
		this.setStudip_user_id(StudIP_user_id);
	}
	public String getStudip_userName() {
		return studip_userName;
	}

	public void setStudip_userName(String studip_userName) {
		this.studip_userName = studip_userName;
		this.getProperties().put("studIP_userName", studip_userName);
	}

	public String getStudip_user_id() {
		return studip_user_id;
	}

	public void setStudip_user_id(String studip_user_id) {
		this.studip_user_id = studip_user_id;
		this.getProperties().put("studIP_userID", studip_user_id);
	}

	public String getStudip_role() {
		return studip_role;
	}

	public void setStudip_role(String studip_role) {
		this.studip_role = studip_role;
		this.getProperties().put("studIP_role", studip_role);

	}

}
