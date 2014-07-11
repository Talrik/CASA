package de.lehsten.casa.contextserver.types.media;

public class Image extends Media {
	
	private String fileType;
	
	public Image(){
		this.setMediaType("Image");
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String format) {
		this.fileType = format;
		this.addProperty("fileType", format);
	}

}
