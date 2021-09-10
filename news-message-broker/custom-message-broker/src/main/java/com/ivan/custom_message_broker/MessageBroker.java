package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;

public class MessageBroker {
    private static final Logger LOGGER = LogManager.getLogger(MessageBroker.class);

    private final ExecutorService threadPool;

    public MessageBroker() {
        this.threadPool = Executors.newFixedThreadPool(10);
    }
}
