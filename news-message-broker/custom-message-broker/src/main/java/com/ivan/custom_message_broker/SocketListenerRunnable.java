package com.ivan.custom_message_broker;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.UUID;

public class SocketListenerRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SocketListenerRunnable.class);
    private final Socket socket;
    private final UUID connectionUuid;
    private final MessageBroker messageBroker;

    public SocketListenerRunnable(Socket socket, UUID connectionUuid, MessageBroker messageBroker) {
        this.socket = socket;
        this.connectionUuid = connectionUuid;
        this.messageBroker = messageBroker;
    }

    // true success, false failed
    private ConnectModel processConnectEvent(BufferedReader br) throws Exception {
        try {

            String data = br.readLine();


            LinkedHashMap<String, Object> connectModelHashSet = JsonUtils.toJavaObject(data);
            ConnectModel connectModel = new ConnectModel();
            connectModel.setConnectionType((String) connectModelHashSet.get("connectionType"));

            return connectModel;

        } catch (Exception e1) {
            throw new Exception("processConnectEvent error: ", e1);
        }
    }

    @Override
    public void run() {
        ConnectModel connectModel = null;

        try (InputStream input = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(isr);) {

            connectModel = processConnectEvent(br);

            if (connectModel.getConnectionType().equals(ConnectModel.RECEIVER_CONNECTION_TYPE)) {
                messageBroker.pushEvent(
                        MessageBrokerEventType.RECEIVER_CONNECTED, connectionUuid);
            }
            if (connectModel.getConnectionType().equals(ConnectModel.SENDER_CONNECTION_TYPE)) {
                messageBroker.pushEvent(
                        MessageBrokerEventType.SENDER_CONNECTED, connectionUuid);
            }

            String data = null;

            while ((data = br.readLine()) != null) {
                log.info("Received message: {}", data);

            }
        } catch (Exception e1) {
            log.error(e1.getMessage());
        } finally {

            // push disconnect event
            if (connectModel != null) {
                if (connectModel.getConnectionType()
                        .equals(ConnectModel.RECEIVER_CONNECTION_TYPE)) {
                    messageBroker.pushEvent(
                            MessageBrokerEventType.RECEIVER_DISCONNECTED, connectionUuid);
                }

                if (connectModel.getConnectionType().equals(ConnectModel.SENDER_CONNECTION_TYPE)) {
                    messageBroker.pushEvent(
                            MessageBrokerEventType.SENDER_DISCONNECTED, connectionUuid);
                }
            }

        }
    }

}
