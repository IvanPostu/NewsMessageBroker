package com.ivan.sender_app.grpc;

import com.ivan.common_module.news.NewsResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsResponseObserver implements StreamObserver<NewsResponse> {
    private static Logger log = LoggerFactory.getLogger(NewsResponseObserver.class);

    @Override
    public void onNext(NewsResponse output) {
        log.info("{}", output);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }

}
