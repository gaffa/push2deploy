package de.upday.tools.pushbutton.application.ui;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.net.URI;

@Service
public class BrowserService {

    public void openUrl(String url) {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(url));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
