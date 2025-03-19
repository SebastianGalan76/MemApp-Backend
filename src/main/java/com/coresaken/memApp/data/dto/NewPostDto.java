package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostFlag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NewPostDto(String text, String content, Post.Type type, Post.Visibility visibility, Set<PostFlag.FlagType> flags, Set<String> hashtags) {
}
