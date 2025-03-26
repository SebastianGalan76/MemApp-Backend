package com.coresaken.memApp.controller.user;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.mapper.UserDtoMapper;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    final UserRepository userRepository;

    @GetMapping("/user")
    public UserDto getUser(){
        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            return null;
        }

        Optional<User> userOptional = userRepository.findById(userService.getLoggedInUser().getId());
        return userOptional.map(UserDtoMapper::toDTO).orElse(null);

    }
}
