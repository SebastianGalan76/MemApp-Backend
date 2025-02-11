package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NewPostDto(String text, String content, Post.Type type, Post.Visibility visibility) {
}
