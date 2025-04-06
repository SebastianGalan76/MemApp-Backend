package com.coresaken.memApp.controller.user;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.user.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController {
    final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<Response> follow(@PathVariable("userId") Long userId){
        return followService.follow(userId);
    }

    @GetMapping("/follow/post/{page}")
    public Page<PostDto> getPosts(@PathVariable("page") int page, HttpServletRequest request){
        return followService.getPosts(page, request);
    }

    @GetMapping("/follow/user/{page}")
    public Page<UserDto> getUsers(
            @PathVariable("page") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "followedAt") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order){
        return followService.getUsers(page, sortBy, order);
    }
}
