package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.UUID;

public class SenderListener implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SenderListener.class);
    private final Socket socket;
    private final UUID senderUuid;

    public SenderListener(Socket socket, UUID senderUuid) {
        this.socket = socket;
        this.senderUuid = senderUuid;
    }

    @Override
    public void run() {
        boolean run = true;

        log.info("Serve sender {}", senderUuid.toString());

        while (run) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                log.info("Received message: {}", message);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Connection with sender: {} closed", senderUuid.toString());
                run = false;
            }
        }
    }

}
