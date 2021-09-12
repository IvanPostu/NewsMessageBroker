package com.ivan.receiver_app.business_logic;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import com.ivan.common_module.models.ConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ReceiverBusinessLogic {
    private static Logger log = LoggerFactory.getLogger(ReceiverBusinessLogic.class);

    /** 
     * TCP Socket resources 
     */
    private Socket messageBrokerSocket;
    private PrintWriter socketWriter;

    public void reconnectAsync(String host, int port, ConnectionType connectionType, String topic) {
        log.info("Reconnect to {} : {}", host, port);

        closeOldResources();

        boolean tcpSuccess = ConnectionType.TCP_SOCKET.equals(connectionType)
                && connectWithSocket(host, port, topic);

        boolean grpcSuccess = ConnectionType.GRPC_PROTO.equals(connectionType)
                && connectWithGrpc(host, port, topic);

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

    public void subscribeToTopic(String topic) {}

    private boolean connectWithSocket(String host, int port, String topic) {
        try {
            messageBrokerSocket = new Socket(
                    InetAddress.getByName(host), port);

            boolean isConnected = messageBrokerSocket.isConnected();
            log.info("Socket is alive: {}", isConnected);

            if (isConnected) {
                socketWriter = new PrintWriter(
                        messageBrokerSocket.getOutputStream());

                ConnectModel model = new ConnectModel();
                model.setConnectionType(String
                        .format("%s%s", ConnectModel.RECEIVER_SUBSCRIBE_TO_TOPIC, topic));

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

    private boolean connectWithGrpc(String host, int port, String topic) {
        throw new RuntimeException("Not implemented");
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
