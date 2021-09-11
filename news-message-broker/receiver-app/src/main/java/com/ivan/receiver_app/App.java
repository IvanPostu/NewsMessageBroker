package com.ivan.receiver_app;

import com.ivan.receiver_app.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            log.info("Receiver window instace created with success");
            new Window("News Message Broker (Receiver)");
        });
    }
}
