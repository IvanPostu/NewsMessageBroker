package com.ivan.common_module.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectModel implements Serializable {

    public static final String SENDER_CONNECTION_TYPE = "SENDER_CONNECTION_TYPE";
    public static final String RECEIVER_CONNECTION_TYPE = "RECEIVER_CONNECTION_TYPE";

    private String connectionType; // SENDER | RECEIVER
}
