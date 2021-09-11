package com.ivan.common_module.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectModel {

    public static final byte SENDER_CONNECTION_TYPE = 1;
    public static final byte RECEIVER_CONNECTION_TYPE = 2;

    private byte connectionType; // SENDER | RECEIVER
}
