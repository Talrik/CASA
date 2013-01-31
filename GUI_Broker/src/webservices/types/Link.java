package webservices.types;

import java.io.Serializable;

/**
 *
 * @author phil
 */

public class Link implements Serializable{
    
    private String title; 
    private String URL;

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public String getTitle() {
        return title;
    }
    
}

