package de.lehsten.casa.contextserver.types.media;

public class File extends Media {
	
	private String fileType;
	
	public File(){
		this.setMediaType("File");
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String format) {
		this.fileType = format;
		this.addProperty("fileType", format);
	}

}
