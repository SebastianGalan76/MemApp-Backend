package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.dto.PostFlagDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.post.PostFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostFlagController {
    final PostFlagService postFlagService;

    @PostMapping("/post-flag")
    public ResponseEntity<Response> setPostFlag(@RequestBody PostFlagDto postFlagDto){
        return postFlagService.setPostFlag(postFlagDto);
    }
}
