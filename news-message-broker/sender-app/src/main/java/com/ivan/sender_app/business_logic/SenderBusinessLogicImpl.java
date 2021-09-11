package com.ivan.sender_app.business_logic;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import com.ivan.common_module.models.NewsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SenderBusinessLogicImpl implements SenderBusinessLogic {
    private static Logger log = LoggerFactory.getLogger(SenderBusinessLogicImpl.class);


    /**
     * TCP Socket resources 
     */
    private Socket messageBrokerSocket;
    private PrintWriter socketWriter;

    /**
     * gRPC resources 
     */

    @Override
    public boolean isConnected() {

        return false;
    }

    @Override
    public void reconnectAsync(String host, int port, ConnectionType connectionType) {
        log.info("Reconnect to {} : {}", host, port);

        closeOldResources();

        boolean tcpSuccess = ConnectionType.TCP_SOCKET.equals(connectionType)
                && connectWithSocket(host, port);

        boolean grpcSuccess = ConnectionType.GRPC_PROTO.equals(connectionType)
                && connectWithGrpc(host, port);

        if (tcpSuccess || grpcSuccess) {
            JOptionPane.showMessageDialog(null,
                    "Connected with success " + connectionType.toString(),
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Connected with error ",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    private boolean connectWithSocket(String host, int port) {
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

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    private boolean connectWithGrpc(String host, int port) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean sendNews(NewsModel newsModel) {


        try {
            String jsonModel = JsonUtils.toJson(newsModel);
            socketWriter.println(jsonModel);
            socketWriter.flush();
            return true;
        } catch (Exception e) {
            log.error("sendNews error: {}", e);
        }

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

    /**
     * Close old socket and grpc resources if exists
     */
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
