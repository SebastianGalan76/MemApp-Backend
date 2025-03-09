package com.coresaken.memApp.controller.user;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.dto.UserProfileDto;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.service.user.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserProfileController {
    final UserProfileService userProfileService;

    @GetMapping("/profile/{login}")
    private ResponseEntity<ObjectResponse<UserProfileDto>> getProfile(@PathVariable("login") String login){
        return userProfileService.getProfile(login);
    }

    @GetMapping("/profile/{login}/post/{page}")
    private Page<PostDto> getUserPosts(@PathVariable("login") String login, @PathVariable("page") int page, HttpServletRequest request){
        return userProfileService.getUserPosts(login, page, request);
    }
}
