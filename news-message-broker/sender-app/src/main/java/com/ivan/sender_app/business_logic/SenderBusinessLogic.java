package com.ivan.sender_app.business_logic;

import com.ivan.common_module.models.ConnectionType;
import com.ivan.common_module.models.NewsModel;

public interface SenderBusinessLogic {


    void reconnectAsync(String host, int port, ConnectionType connectionType);

    boolean sendNews(NewsModel newsModel);


}
