package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    final PostService postService;

    @GetMapping("post/all")
    public List<PostDto> getAllPosts(HttpServletRequest request){
        return postService.getAllPosts(request);
    }
}
