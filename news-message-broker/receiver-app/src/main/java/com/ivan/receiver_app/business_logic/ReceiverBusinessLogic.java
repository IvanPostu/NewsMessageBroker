package com.ivan.receiver_app.business_logic;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.ConnectModel;
import com.ivan.common_module.models.ConnectionType;
import com.ivan.common_module.models.NewsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class ReceiverBusinessLogic {
    private static Logger log = LoggerFactory.getLogger(ReceiverBusinessLogic.class);

    /** 
     * TCP Socket resources 
     */
    private Socket messageBrokerSocket;
    private PrintWriter socketWriter;

    private Consumer<NewsModel> receiveCallback = (m) -> {
    };

    public void setReceiveCallback(Consumer<NewsModel> receiveCallback) {
        this.receiveCallback = receiveCallback;
    }

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

        if (tcpSuccess) {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() {
                    try (InputStream input = messageBrokerSocket.getInputStream();
                            InputStreamReader isr = new InputStreamReader(input);
                            BufferedReader br = new BufferedReader(isr);) {

                        String data = null;

                        while ((data = br.readLine()) != null) {
                            processMessage(data);
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    return null;
                }
            };
            worker.execute();
        }

    }

    private void processMessage(String message) {
        LinkedHashMap<String, Object> newsModel;
        try {
            newsModel = JsonUtils.toJavaObject(message);
            NewsModel newModel = new NewsModel();
            newModel.setAuthor((String) newsModel.get("author"));
            newModel.setCategory((String) newsModel.get("category"));
            newModel.setContent((String) newsModel.get("content"));
            newModel.setTopic((String) newsModel.get("topic"));

            this.receiveCallback.accept(newModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

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
