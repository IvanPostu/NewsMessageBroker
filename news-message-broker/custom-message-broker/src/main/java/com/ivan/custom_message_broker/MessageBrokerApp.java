package com.ivan.custom_message_broker;

import com.ivan.custom_message_broker.grpc.RPCServer;
import com.ivan.custom_message_broker.tcp.SocketServer;
import com.ivan.custom_message_broker.tcp.TCPMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageBrokerApp {
    private static Logger log = LoggerFactory.getLogger(MessageBrokerApp.class);

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        boolean isTcp = false;

        try {
            String type = args[0];
            
            if (type.equals("grpc")) {
                log.info("Start grpc message broker app");
                isTcp = false;
            } else {
                if (type.equals("tcp")) {
                    log.info("Start tcp message broker app");
                    isTcp = true;
                } else {

                    throw new Exception();
                }
            }
        } catch (Exception e) {
            log.info("Please set first argument \"grpc\" or \"tcp\"");
        }

        MessageBrokerApp.run(isTcp);
    }

    private static void run(boolean isTcp) {
        Runnable server;

        if (isTcp) {
            TCPMessageBroker messageBroker = new TCPMessageBroker();
            server = new SocketServer(messageBroker);
        } else {
            server = new RPCServer();
        }

        server.run();
    }
}
