package com.ivan.receiver_app.grpc;

import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsResponse;
import com.ivan.common_module.news.NewsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public class NewsGrpcClient implements Closeable {

    private ManagedChannel channel;
    private NewsServiceGrpc.NewsServiceStub clientStub;
    private Consumer<NewsResponse> consumer;

    public NewsGrpcClient(String host, int port, Consumer<NewsResponse> consumer) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.clientStub = NewsServiceGrpc.newStub(channel);
        this.consumer = consumer;
    }

    public void send(NewsRequest news) {
        StreamObserver<NewsRequest> inputStreamObserver = this.clientStub.send(new NewsResponseObserver(consumer));

        inputStreamObserver.onNext(news);

        // client side is done. this method make the server respond with the sum value
        // inputStreamObserver.onCompleted();
    }

    @Override
    public void close() throws IOException {
        try {
            this.channel.shutdown();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
