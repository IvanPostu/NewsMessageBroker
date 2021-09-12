package com.ivan.sender_app;

import com.ivan.sender_app.business_logic.SenderBusinessLogicImpl;
import com.ivan.sender_app.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class SenderApp {
    private static Logger log = LoggerFactory.getLogger(SenderApp.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            log.info("Window instace created with success");
            new Window("News Message Broker (Sender)", new SenderBusinessLogicImpl());
        });
    }
}
