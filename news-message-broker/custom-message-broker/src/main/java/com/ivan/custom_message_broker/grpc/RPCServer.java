package com.ivan.custom_message_broker.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RPCServer.class);

    @Override
    public void run() {
        try {

            Server server = ServerBuilder.forPort(6565).addService(new NewsService()).build();

            // start
            server.start();

            // shutdown hook
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
