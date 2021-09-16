package com.ivan.sender_app.business_logic;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import com.ivan.common_module.models.ConnectionType;
import com.ivan.common_module.models.NewsModel;
import com.ivan.sender_app.grpc.NewsGrpcClient;
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
    private NewsGrpcClient newsGrpcClient;


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
        newsGrpcClient = new NewsGrpcClient(host, port);

        return true;
    }

    @Override
    public boolean sendNews(NewsModel newsModel) {


        try {
            String jsonModel = JsonUtils.toJson(newsModel);

            if (socketWriter != null) {
                log.info("Send data using tcp");
                socketWriter.println(jsonModel);
                socketWriter.flush();
                return true;
            }

            if (newsGrpcClient != null) {
                log.info("Send data using grpc");
                newsGrpcClient.send(newsModel);
                return true;
            }

            log.warn("Connection not found");
            return false;
        } catch (Exception e) {
            log.error("sendNews error: {}", e);
        }

        return false;
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

        if (newsGrpcClient != null) {
            try {
                newsGrpcClient.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            newsGrpcClient = null;
        }
    }
}
