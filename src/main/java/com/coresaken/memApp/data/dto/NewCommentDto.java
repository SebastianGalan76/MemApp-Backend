package com.coresaken.memApp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    String content;
    Type type;
    Long id;

    public enum Type{
        POST, COMMENT;
    }
}
