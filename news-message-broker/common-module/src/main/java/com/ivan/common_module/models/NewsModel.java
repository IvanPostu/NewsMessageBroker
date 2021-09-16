package com.ivan.common_module.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsModel extends MessageBrokerEntity implements Serializable {

    private String category;
    private String author;
    private String content;
}
