package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStream input;
        try {
            input = socket.getInputStream();


            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            String data = null;

            log.info("Serve sender {}", senderUuid.toString());


            while ((data = br.readLine()) != null) {
                try {
                    log.info("Received message: {}", data);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.error("Connection with sender: {} closed", senderUuid.toString());
                }
            }
        } catch (IOException e1) {
            log.error(e1.getMessage());
            return;
        }
    }

}
