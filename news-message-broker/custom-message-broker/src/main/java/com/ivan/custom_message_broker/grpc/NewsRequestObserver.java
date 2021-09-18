package com.ivan.custom_message_broker.grpc;

import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class NewsRequestObserver implements StreamObserver<NewsRequest> {
    private static final Logger log = LoggerFactory.getLogger(RPCServer.class);
    private static final GRPCMessageBroker BROKER = new GRPCMessageBroker();;
    private final StreamObserver<NewsResponse> newsResponseStreamObserver;
    private final UUID uuid = UUID.randomUUID();
    private boolean isReceiver = false;

    private String subscribedTopic = "";

    public NewsRequestObserver(StreamObserver<NewsResponse> newsResponseStreamObserver) {
        this.newsResponseStreamObserver = newsResponseStreamObserver;
    }

    @Override
    public void onNext(NewsRequest value) {

        if (value.getTopic().startsWith("subscribe:")) {
            isReceiver = true;
            subscribedTopic = value.getTopic().substring(10);
            BROKER.receiverConnected(uuid, subscribedTopic, (req) -> {
                NewsResponse response = NewsResponse.newBuilder().setAuthor(req.getAuthor())
                        .setCategory(req.getCategory()).setContent(req.getContent()).setTopic(req.getTopic()).build();

                this.newsResponseStreamObserver.onNext(response);
            });

        } else {
            BROKER.publishMessage(value);
            this.newsResponseStreamObserver.onCompleted();
        }

    }

    @Override
    public void onError(Throwable t) {
        log.error("NewsRequestObserver onError: {}", t);
        if (isReceiver) {
            BROKER.receiverDisconnected(uuid, subscribedTopic);
        }
    }

    @Override
    public void onCompleted() {

        log.info("Client reached safely");
        if (isReceiver) {
            BROKER.receiverDisconnected(uuid, subscribedTopic);
        }
    }

}
