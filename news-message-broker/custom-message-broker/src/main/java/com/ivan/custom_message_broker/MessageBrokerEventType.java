package com.ivan.custom_message_broker;

import com.ivan.common_module.models.NewsModel;
import java.lang.reflect.Type;
import java.util.UUID;

public enum MessageBrokerEventType {

    SUBSCRIBE {
        public Type getPayloadType() {
            return NewsModel.class;
        }
    }, //for receiver
    PUBLISH {
        public Type getPayloadType() {
            return NewsModel.class;
        }
    }, //for sender

    SENDER_CONNECTED {
        public Type getPayloadType() {
            return UUID.class;
        }
    },
    SENDER_DISCONNECTED {
        public Type getPayloadType() {
            return UUID.class;
        }
    },
    RECEIVER_CONNECTED {
        public Type getPayloadType() {
            return UUID.class;
        }
    },
    RECEIVER_DISCONNECTED {
        public Type getPayloadType() {
            return UUID.class;
        }
    };

    public abstract Type getPayloadType();
}
