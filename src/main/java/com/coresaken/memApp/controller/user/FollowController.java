package com.coresaken.memApp.controller.user;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.user.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {
    final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<Response> follow(@PathVariable("userId") Long userId){
        return followService.follow(userId);
    }
}
