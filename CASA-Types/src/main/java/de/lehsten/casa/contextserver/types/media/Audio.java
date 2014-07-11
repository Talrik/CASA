package de.lehsten.casa.contextserver.types.media;

public class Audio extends Media {
	
	private String fileType;
	
	public Audio(){
		this.setMediaType("Audio");
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String format) {
		this.fileType = format;
		this.addProperty("fileType", format);
	}
}
