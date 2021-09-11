package com.ivan.sender_app.business_logic;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SenderBusinessLogicImpl implements SenderBusinessLogic {
    private static Logger log = LoggerFactory.getLogger(SenderBusinessLogicImpl.class);

    private Socket messageBrokerSocket;
    private PrintWriter socketWriter;

    @Override
    public boolean isConnected() {

        return false;
    }

    @Override
    public void reconnectAsync(String host, int port) {
        log.info("Reconnect to {} : {}", host, port);

        closeOldResources();

        try {
            messageBrokerSocket = new Socket(
                    InetAddress.getByName(host), port);

            boolean isConnected = messageBrokerSocket.isConnected();
            log.info("Socket is alive: {}", isConnected);

            if (isConnected) {
                socketWriter = new PrintWriter(
                        messageBrokerSocket.getOutputStream());

                ConnectModel model = new ConnectModel();
                model.setConnectionType(ConnectModel.SENDER_CONNECTION_TYPE);

                String jsonModel = JsonUtils.toJson(model);

                socketWriter.println(jsonModel);
                socketWriter.flush();
            }

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

    private void closeOldResources() {
        if (messageBrokerSocket != null) {
            try {
                messageBrokerSocket.close();
                messageBrokerSocket = null;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        if (socketWriter != null) {
            try {
                socketWriter.close();
                socketWriter = null;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
