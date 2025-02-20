package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
    Long id;
    String content;
    AuthorDto author;
    boolean isEdited;

    ReplyDto reply;
    LocalDateTime createdAt;

    int likeAmount;
    int dislikeAmount;

    int userRating;

    public void setAuthor(User user) {
        this.author = new AuthorDto(user.getId(), user.getLogin());
    }

    @Data
    @AllArgsConstructor
    public static class AuthorDto {
        Long id;
        String login;
    }

    @Data
    public static class ReplyDto{
        int totalReplies;
        int currentPage;
        List<CommentDto> replies;

        public ReplyDto(int totalReplies, List<CommentDto> replies) {
            this.totalReplies = totalReplies;
            this.replies = replies;
            this.currentPage = 0;
        }
    }
}
