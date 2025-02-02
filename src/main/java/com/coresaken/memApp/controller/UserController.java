package com.coresaken.memApp.controller;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.mapper.UserDtoMapper;
import com.coresaken.memApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("/user")
    public UserDto getUser(){
        return UserDtoMapper.toDTO(userService.getLoggedInUser());
    }
}
