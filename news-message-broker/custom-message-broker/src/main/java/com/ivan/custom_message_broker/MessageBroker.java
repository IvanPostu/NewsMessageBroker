package com.ivan.custom_message_broker;

import com.ivan.common_module.JsonUtils;
import com.ivan.common_module.models.NewsModel;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageBroker {
    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);

    private final Map<String, Queue<NewsModel>> data;

    //receiverId - [writer, topic]
    private final Map<String, List<Pair<PrintWriter, UUID>>> receivers;

    public MessageBroker() {
        data = new ConcurrentHashMap<>();
        receivers = new ConcurrentHashMap<>();
    }

    public void receiverConnected(UUID receiverUuid, PrintWriter writer, String topic) {
        log.info("Receiver with uuid: {} and topic: {} connected", receiverUuid, topic);

        List<Pair<PrintWriter, UUID>> element;
        if (receivers.containsKey(topic)) {
            element = receivers.get(topic);
        } else {
            element = new ArrayList<Pair<PrintWriter, UUID>>();
            receivers.put(topic, element);
        }

        element.add(Pair.with(writer, receiverUuid));

        tryToSendMessagesFromQueueToReceivers();
    }

    public void receiverDisconnected(UUID receiverUuid, String topic) {
        log.info("Trying to disconnect receiver with uid: {}", receiverUuid);


        if (receivers.containsKey(topic)) {
            List<Pair<PrintWriter, UUID>> subscribedReceivers = receivers.get(topic);
            Iterator<Pair<PrintWriter, UUID>> itr = subscribedReceivers.iterator();
            while (itr.hasNext()) {
                Pair<PrintWriter, UUID> x = itr.next();
                if (receiverUuid == x.getValue1()) {
                    itr.remove();
                    log.info("Receiver with uuid: {} disconnected", receiverUuid);

                    return;
                }
            }

        }

    }

    public void senderConnected(UUID senderUuid) {
        log.info("Sender with uuid: {} connected", senderUuid);
    }

    public void senderDisconnected(UUID senderUuid) {
        log.info("Sender with uuid: {} disconnected", senderUuid);
    }

    public void publishMessage(NewsModel newsModel) {
        log.info("Publish model with data: {}", newsModel);
        Queue<NewsModel> targetQueue;
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

        tryToSendMessagesFromQueueToReceivers();
    }

    public void tryToSendMessagesFromQueueToReceivers() {
        try {
            for (Map.Entry<String, Queue<NewsModel>> e : data.entrySet()) {

                List<Pair<PrintWriter, UUID>> recs = receivers.get(e.getKey());
                Queue<NewsModel> messages = e.getValue();
                NewsModel message = null;
                if (recs != null && recs.size() > 0 && messages != null && !messages.isEmpty()) {
                    while ((message = messages.poll()) != null) {

                        for (Pair<PrintWriter, UUID> rec : recs) {
                            String jsonModel = JsonUtils.toJson(message);

                            rec.getValue0().println(jsonModel);
                            rec.getValue0().flush();
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
