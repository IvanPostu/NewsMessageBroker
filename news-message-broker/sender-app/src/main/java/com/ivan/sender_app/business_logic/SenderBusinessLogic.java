package com.ivan.sender_app.business_logic;

public interface SenderBusinessLogic {

    boolean isConnected();

    void connectAsync();

    boolean putMessageInLocalQueue(String category, String message);

    void runLongPoolingCall(String host, String port);

    boolean longPoolingRunning();

    void stopLongPoolingCall(String host, String port);

}
