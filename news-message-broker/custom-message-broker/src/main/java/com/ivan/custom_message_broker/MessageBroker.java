package com.ivan.custom_message_broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBroker {
    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);

    // private final ExecutorService threadPool;

    // private final ConcurrentHashMap<UUID, Socket> senders;
    // private final ConcurrentHashMap<UUID, Socket> receivers;

    public MessageBroker() {
        // this.threadPool = Executors.newFixedThreadPool(10);

        // this.senders = new ConcurrentHashMap<UUID, Socket>();
        // this.receivers = new ConcurrentHashMap<UUID, Socket>();
    }

    public void pushEvent(MessageBrokerEventType type, Object payload) {
        log.info("Push event: {}", type.toString());
    }
}
