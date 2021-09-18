package com.ivan.custom_message_broker.grpc;

import com.ivan.common_module.news.NewsRequest;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class GRPCMessageBroker {
    private static final Logger log = LoggerFactory.getLogger(GRPCMessageBroker.class);
    private final Map<String, Queue<NewsRequest>> data;
    private final Map<String, List<Pair<UUID, Consumer<NewsRequest>>>> receivers;

    public GRPCMessageBroker() {
        data = new ConcurrentHashMap<>();
        receivers = new ConcurrentHashMap<>();
    }

    public void receiverConnected(UUID uuid, String topic, Consumer<NewsRequest> writer) {
        log.info("Receiver with uuid: {} and topic: {} connected", uuid, topic);

        List<Pair<UUID, Consumer<NewsRequest>>> element;
        if (receivers.containsKey(topic)) {
            element = receivers.get(topic);
        } else {
            element = new ArrayList<Pair<UUID, Consumer<NewsRequest>>>();
            receivers.put(topic, element);
        }

        element.add(Pair.with(uuid, writer));

        tryToSendMessagesFromQueueToReceivers();
    }

    public void receiverDisconnected(UUID receiverUuid, String topic) {
        log.info("Trying to disconnect receiver with uid: {}", receiverUuid);

        if (receivers.containsKey(topic)) {
            List<Pair<UUID, Consumer<NewsRequest>>> subscribedReceivers = receivers.get(topic);
            Iterator<Pair<UUID, Consumer<NewsRequest>>> itr = subscribedReceivers.iterator();
            while (itr.hasNext()) {
                Pair<UUID, Consumer<NewsRequest>> x = itr.next();
                if (receiverUuid == x.getValue0()) {
                    itr.remove();
                    log.info("Receiver with uuid: {} disconnected", receiverUuid);

                    return;
                }
            }

        }
    }

    public void publishMessage(NewsRequest newsModel) {
        log.info("Publish model with data: {}", newsModel);
        Queue<NewsRequest> targetQueue;
        try {
            targetQueue = data.get(newsModel.getTopic());
        } catch (Exception e) {
            targetQueue = new ConcurrentLinkedQueue<>();
        }

        if (targetQueue == null) {
            targetQueue = new ConcurrentLinkedQueue<>();
            data.put(newsModel.getTopic(), targetQueue);
        }

        targetQueue.add(newsModel);

        // tryToSendMessagesFromQueueToReceivers();
    }

    public void tryToSendMessagesFromQueueToReceivers() {
        try {
            for (Map.Entry<String, Queue<NewsRequest>> e : data.entrySet()) {

                List<Pair<UUID, Consumer<NewsRequest>>> recs = receivers.get(e.getKey());
                Queue<NewsRequest> messages = e.getValue();
                NewsRequest message = null;
                if (recs != null && recs.size() > 0 && messages != null && !messages.isEmpty()) {
                    while ((message = messages.poll()) != null) {

                        for (Pair<UUID, Consumer<NewsRequest>> rec : recs) {
                            rec.getValue1().accept(message);
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
