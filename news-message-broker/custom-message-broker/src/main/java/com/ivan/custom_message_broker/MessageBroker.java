package com.ivan.custom_message_broker;

import com.ivan.common_module.models.NewsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBroker {
    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);

    private Map<String, Queue<Object>> data = new ConcurrentHashMap<>();

    private Map<String, UUID> receivers = new ConcurrentHashMap<>();

    public MessageBroker() {

    }

    public void receiverConnected(UUID receiverUuid) {
        log.info("Receiver with uuid: {} connected", receiverUuid);
    }

    public void senderConnected(UUID senderUuid) {
        log.info("Sender with uuid: {} connected", senderUuid);
    }

    public void receiverDisconnected(UUID receiverUuid) {
        log.info("Receiver with uuid: {} disconnected", receiverUuid);
    }

    public void senderDisconnected(UUID senderUuid) {
        log.info("Sender with uuid: {} disconnected", senderUuid);
    }

    public void publishMessage(NewsModel newsModel) {
        log.info("Publish model with data: {}", newsModel);
    }

}
