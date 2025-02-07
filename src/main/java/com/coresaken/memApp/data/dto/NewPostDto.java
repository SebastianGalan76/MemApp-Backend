package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.post.Post;

public record NewPostDto(String text, String content, Post.Visibility visibility, Post.Type type, boolean contentSpoiler, boolean contentNSFW) {
}
