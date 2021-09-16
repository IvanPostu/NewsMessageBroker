package com.ivan.custom_message_broker.grpc;

import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsResponse;
import io.grpc.stub.StreamObserver;

public class NewsRequestObserver implements StreamObserver<NewsRequest> {
    private final StreamObserver<NewsResponse> newsResponseStreamObserver;

    public NewsRequestObserver(StreamObserver<NewsResponse> newsResponseStreamObserver) {
        this.newsResponseStreamObserver = newsResponseStreamObserver;
    }

    @Override
    public void onNext(NewsRequest value) {
        NewsResponse response = NewsResponse.newBuilder()
                .setOk(1)
                .build();


        this.newsResponseStreamObserver.onNext(response);
    }

    @Override
    public void onError(Throwable t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCompleted() {
        this.newsResponseStreamObserver.onCompleted();
        System.out.println("Client reached safely");
    }

}
