package com.ivan.custom_message_broker.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class RPCServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RPCServer.class);
    private static final int SERVER_PORT = 8080;

    @Override
    public void run() {
        try {

            Server server = ServerBuilder.forPort(SERVER_PORT).executor(Executors.newFixedThreadPool(30))
                    .addService(new NewsService()).build();

            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("RPC server is shutting down!");
                server.shutdown();
            }));

            server.awaitTermination();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
