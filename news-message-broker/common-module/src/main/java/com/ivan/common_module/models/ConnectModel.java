package com.ivan.common_module.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ConnectModel implements Serializable {

    public static final String SENDER_CONNECTION_TYPE = "SENDER_CONNECTION_TYPE";
    public static final String RECEIVER_SUBSCRIBE_TO_TOPIC = "RECEIVER_SUBSCRIBE_TO_TOPIC";

    private String connectionType;

}
