package com.coresaken.memApp.data;

import com.coresaken.memApp.database.model.list.UserPostList;
import lombok.Data;

import java.util.List;

@Data
public class UserDto{
    Long id;
    String login;
    String email;
    String role;

    String avatar;

    List<UserPostList> ownedMemeLists;
}

