package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    Long id;
    String login;
    String avatar;
    User.Role role;

    boolean following;

    List<PostListDto> userList;

    public record PostListDto(Long id, UUID uuid, String name, int postAmount){

    }
}
