package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    LocalDateTime createdAt;
    List<CommentDto> replies;

    int likeAmount;
    int dislikeAmount;

    int userRating;

    public void setAuthor(User user) {
        this.author = new AuthorDto(user.getId(), user.getLogin());
    }

    @Data
    public static class AuthorDto {
        Long id;
        String login;

        public AuthorDto(Long id, String login){
            this.id = id;
            this.login = login;
        }
    }
}
