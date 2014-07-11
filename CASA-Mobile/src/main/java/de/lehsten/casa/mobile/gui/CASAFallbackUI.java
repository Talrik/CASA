package de.lehsten.casa.mobile.gui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CASAFallbackUI extends UI {
    // FIXME review message
    private static final String MSG = "<h1>Ooops...</h1> <p>You accessed Vornitologist "
            + "with a browser that is not supported. "
            + "Vornitologist is "
            + "ment to be used with modern webkit based mobile browsers, "
            + "e.g. with iPhone or modern Android device. Curretly those "
            + "cover huge majority of actively used mobile browsers. "
            + "Support will be extended as other mobile browsers develop "
            + "and gain popularity. Testing ought to work with desktop "
            + "Safari or Chrome as well.<p>"
            + "<p>The source code for this app (maven built project) is publicly "
            + "available via <a href=\"http://dev.vaadin.com/svn/demo/vornitologist/\">"
            + "SVN</a> or it can be browsed "
            + "<a href=\"http://dev.vaadin.com/browser/demo/vornitologist/\">online</a>. "
            + "There is also a <a href=\""
            // + getURL()
            + "VAADIN/tutorial/touchkit-tutorial.html\">tutorial</a> to help exploring"
            + " how this application is built</p>";
    @Override
    protected void init(VaadinRequest request) {
        Label label = new Label(MSG, ContentMode.HTML);
       
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.addComponent(label);
        setContent(content);
    }

}
