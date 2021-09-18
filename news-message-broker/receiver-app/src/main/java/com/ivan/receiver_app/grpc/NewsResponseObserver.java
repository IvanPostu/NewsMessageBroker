package com.ivan.receiver_app.grpc;

import com.ivan.common_module.news.NewsResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class NewsResponseObserver implements StreamObserver<NewsResponse> {
    private static Logger log = LoggerFactory.getLogger(NewsResponseObserver.class);
    private Consumer<NewsResponse> consumer;

    public NewsResponseObserver(Consumer<NewsResponse> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onNext(NewsResponse output) {
        log.info("{}", output);
        consumer.accept(output);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("Completed");
    }

}
