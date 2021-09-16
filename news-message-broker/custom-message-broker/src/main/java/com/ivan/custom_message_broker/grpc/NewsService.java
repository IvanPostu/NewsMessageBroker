package com.ivan.custom_message_broker.grpc;

import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsResponse;
import com.ivan.common_module.news.NewsServiceGrpc.NewsServiceImplBase;
import io.grpc.stub.StreamObserver;

public class NewsService extends NewsServiceImplBase {

    @Override
    public StreamObserver<NewsRequest> send(StreamObserver<NewsResponse> responseObserver) {
        return new NewsRequestObserver(responseObserver);
    }

}
