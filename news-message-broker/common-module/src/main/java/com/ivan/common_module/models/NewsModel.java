package com.ivan.common_module.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class NewsModel implements Serializable {

    private String category;
    private String author;
    private String content;
}
