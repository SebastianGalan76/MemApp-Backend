package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    final PostService postService;

    @GetMapping("post/home/{page}")
    public Page<PostDto> getHomePosts(@PathVariable("page") int page, HttpServletRequest request){
        return postService.getHomePosts(page, request);
    }

    @GetMapping("post/waiting/{page}")
    public Page<PostDto> getWaitingRoomPosts(@PathVariable("page") int page, HttpServletRequest request){
        return postService.getWaitingRoomPosts(page, request);
    }

    @GetMapping("post/tag/{tag}/{page}")
    public Page<PostDto> getPostsByHashtag(@PathVariable("tag") String tag, @PathVariable("page") int page, HttpServletRequest request){
        return postService.getPostsByTag(tag, page, request);
    }

    @GetMapping("post/{id}")
    public ResponseEntity<PostDto> getPostDtoById(@PathVariable("id") Long id, HttpServletRequest request){
        return postService.getPostDto(id, request);
    }
}
