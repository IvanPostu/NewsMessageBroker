package com.ivan.custom_message_broker.grpc;

import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsRequestObserver implements StreamObserver<NewsRequest> {
    private static final Logger log = LoggerFactory.getLogger(RPCServer.class);
    private final StreamObserver<NewsResponse> newsResponseStreamObserver;

    public NewsRequestObserver(StreamObserver<NewsResponse> newsResponseStreamObserver) {
        this.newsResponseStreamObserver = newsResponseStreamObserver;
    }

    @Override
    public void onNext(NewsRequest value) {
        NewsResponse response = NewsResponse.newBuilder()
                .setAuthor("qwe")
                .setCategory("zzz")
                .setContent("QQQQQQQ")
                .build();


        this.newsResponseStreamObserver.onNext(response);
    }

    @Override
    public void onError(Throwable t) {
        log.error("NewsRequestObserver onError: {}", t);
    }

    @Override
    public void onCompleted() {
        this.newsResponseStreamObserver.onCompleted();
        System.out.println("Client reached safely");
    }

}
