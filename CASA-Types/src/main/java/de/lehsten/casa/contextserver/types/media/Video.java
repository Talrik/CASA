package de.lehsten.casa.contextserver.types.media;

public class Video extends Media {
	
	private String fileType;
	
	public Video(){
		this.setMediaType("Video");
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String format) {
		this.fileType = format;
		this.addProperty("fileType", format);
	}

}
