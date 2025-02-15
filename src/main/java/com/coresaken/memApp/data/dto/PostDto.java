package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    Long id;
    OwnerDto owner;

    String text;
    String content;

    Post.Type type;
    Post.Visibility visibility;

    LocalDateTime createdAt;

    int rating;

    UserDto user;

    public void setOwner(User user) {
        this.owner = new OwnerDto(user.getId(), user.getLogin());
    }

    @Data
    public static class UserDto {
        byte rating;
        List<Long> postListIds;
    }


    @Data
    public static class OwnerDto {
        Long id;
        String login;

        public OwnerDto(Long id, String login){
            this.id = id;
            this.login = login;
        }
    }
}
