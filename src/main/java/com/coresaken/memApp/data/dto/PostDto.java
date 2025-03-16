package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostFlag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto {
    Long id;
    UUID uuid;

    AuthorDto author;

    String text;
    String content;

    Post.Type type;
    Post.Visibility visibility;

    LocalDateTime createdAt;

    int rating;
    int commentAmount;

    UserDto user;

    List<PostFlag> flags;

    public void setAuthor(User user) {
        if(user != null){
            this.author = new AuthorDto(user.getId(), user.getLogin(), user.getAvatar());
        }
    }

    @Data
    public static class UserDto {
        byte rating;
        List<Long> postListIds;
    }
}
