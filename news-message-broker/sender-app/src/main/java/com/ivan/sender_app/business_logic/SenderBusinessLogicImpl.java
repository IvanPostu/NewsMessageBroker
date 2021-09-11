package com.ivan.sender_app.business_logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SenderBusinessLogicImpl implements SenderBusinessLogic {
    private static Logger log = LoggerFactory.getLogger(SenderBusinessLogicImpl.class);

    private Socket messageBrokerSocket;

    @Override
    public boolean isConnected() {
        try {
            return messageBrokerSocket != null
                    && messageBrokerSocket.isConnected()
                    && messageBrokerSocket.getInputStream().read() != -1;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public void reconnectAsync(String host, int port) {
        log.info("Reconnect to {} : {}", host, port);

        if (messageBrokerSocket != null) {
            try {
                messageBrokerSocket.close();
                messageBrokerSocket = null;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        try {
            messageBrokerSocket = new Socket(
                    InetAddress.getByName(host), port);
            log.info("Socket is alive: {}", messageBrokerSocket.isConnected());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean putMessageInLocalQueue(String category, String message) {
        return false;
    }

    @Override
    public void runLongPoolingCall(String host, String port) {

    }

    @Override
    public boolean longPoolingRunning() {
        return false;
    }

    @Override
    public void stopLongPoolingCall(String host, String port) {

    }

}
