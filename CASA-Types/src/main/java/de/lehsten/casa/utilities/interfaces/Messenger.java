package de.lehsten.casa.utilities.interfaces;

public interface Messenger {
	
	/**
	 * @param receiver  String to identify the receiver
	 */
	public void setReceiver(String receiver);
	/**
	 * @param receiver  String to identify the receiver if it is in the same application
	 */
	public void setLocalReceiver(String receiver);
	/**
	 * @param receiver  String to identify the receiver if it is in the same VM
	 */
	public void setRemoteReceiver(String receiver);
	
	/**
	 * @param owner  String to identify the sender
	 */
	public void setOwner(String owner);
	
	/**Sends a {@link Object}
	 * @param msg     {@link Object} to send
	 */
	public void send(Object o);
	
	/**Sends a {@link String}
	 * @param msg     {@link String} to send
	 */
	public void sendText(String msg);
	
	/**Sends an Object as request and receives an Object as response
	 * @param o Object to send 
	 * @return {@link Object}
	 */
	public Object request(Object o);
	
	
}

