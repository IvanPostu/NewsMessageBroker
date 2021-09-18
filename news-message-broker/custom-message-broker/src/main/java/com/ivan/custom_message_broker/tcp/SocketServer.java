package com.ivan.custom_message_broker.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SocketServer.class);
    private static final int SERVER_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 20;

    private final TCPMessageBroker messageBroker;
    private final ExecutorService threadPool;

    public SocketServer(TCPMessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            log.info("Server is up on port: {}, thread pull size: {}", SERVER_PORT, THREAD_POOL_SIZE);
            while (true) {
                Socket socket = server.accept();
                UUID generatedUuid = UUID.randomUUID();
                threadPool.submit(new SocketListenerRunnable(socket, generatedUuid, messageBroker));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
