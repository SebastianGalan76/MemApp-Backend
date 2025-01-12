package com.coresaken.memApp.data;

import lombok.Data;

@Data
public class UserDto{
    Long id;
    String login;
    String email;
    String role;
}

