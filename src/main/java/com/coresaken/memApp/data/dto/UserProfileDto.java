package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    Long id;
    String login;
    String avatar;
    User.Role role;

    List<PostListDto> userList;

    public record PostListDto(Long id, String name, int postAmount){

    }
}
