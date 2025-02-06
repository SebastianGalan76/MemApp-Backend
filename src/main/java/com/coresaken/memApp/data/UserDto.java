package com.coresaken.memApp.data;

import com.coresaken.memApp.database.model.UserMemeList;
import lombok.Data;

import java.util.List;

@Data
public class UserDto{
    Long id;
    String login;
    String email;
    String role;

    List<UserMemeList> ownedMemeLists;
}

