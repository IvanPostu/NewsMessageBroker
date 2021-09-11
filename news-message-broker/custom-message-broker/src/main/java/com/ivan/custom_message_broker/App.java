package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class App {
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        log.info("Start message broker app");
        MessageBroker messageBroker = new MessageBroker();
        SocketServer socketServer = new SocketServer(messageBroker);

        socketServer.run();
    }
}
