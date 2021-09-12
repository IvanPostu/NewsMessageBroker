package com.ivan.custom_message_broker;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import com.ivan.common_module.models.NewsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        String topic = "";

        try (InputStream input = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(isr);
                PrintWriter socketWriter = new PrintWriter(
                        socket.getOutputStream());) {

            connectModel = processConnectEvent(br);


            if (connectModel.getConnectionType().equals(ConnectModel.SENDER_CONNECTION_TYPE)) {
                messageBroker.senderConnected(connectionUuid);
            }
            if (connectModel.getConnectionType()
                    .startsWith(ConnectModel.RECEIVER_SUBSCRIBE_TO_TOPIC)) {

                topic = connectModel.getConnectionType()
                        .substring(ConnectModel.RECEIVER_SUBSCRIBE_TO_TOPIC.length());

                messageBroker.receiverConnected(connectionUuid, socketWriter, topic);
            }

            String data = null;

            while ((data = br.readLine()) != null) {
                processSenderMessage(data);

            }
        } catch (Exception e1) {
            log.error(e1.getMessage());
        } finally {

            if (connectModel != null) {
                if (connectModel.getConnectionType()
                        .startsWith(ConnectModel.RECEIVER_SUBSCRIBE_TO_TOPIC)) {

                    messageBroker.receiverDisconnected(connectionUuid, topic);
                }

                if (connectModel.getConnectionType().equals(ConnectModel.SENDER_CONNECTION_TYPE)) {
                    messageBroker.senderDisconnected(connectionUuid);
                }
            }
        }
    }

    private void processSenderMessage(String message) {
        log.info("Socket received message: {}", message);

        try {
            LinkedHashMap<String, Object> newsModel = JsonUtils.toJavaObject(message);
            NewsModel newModel = new NewsModel();
            newModel.setAuthor((String) newsModel.get("author"));
            newModel.setCategory((String) newsModel.get("category"));
            newModel.setContent((String) newsModel.get("content"));
            newModel.setTopic((String) newsModel.get("topic"));

            messageBroker.publishMessage(newModel);

        } catch (Exception e) {
            log.warn("processSenderMessage error: {}", e.getMessage());
        }
    }

}
