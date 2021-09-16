package com.ivan.sender_app.grpc;

import com.ivan.common_module.models.NewsModel;
import com.ivan.common_module.news.NewsRequest;
import com.ivan.common_module.news.NewsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.io.Closeable;
import java.io.IOException;

public class NewsGrpcClient implements Closeable {

    private ManagedChannel channel;
    private NewsServiceGrpc.NewsServiceStub clientStub;

    public NewsGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.clientStub = NewsServiceGrpc.newStub(channel);
    }

    public void send(NewsModel news) {
        StreamObserver<NewsRequest> inputStreamObserver =
                this.clientStub.send(new NewsResponseObserver());

        NewsRequest req = NewsRequest.newBuilder().setAuthor(news.getAuthor())
                .setCategory(news.getCategory())
                .setContent(news.getContent())
                .setTopic(news.getTopic())
                .build();
        inputStreamObserver.onNext(req);

        // client side is done. this method make the server respond with the sum value
        inputStreamObserver.onCompleted();
    }

    @Override
    public void close() throws IOException {
        try {

        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
