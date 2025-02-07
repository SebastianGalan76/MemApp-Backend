package com.coresaken.memApp.controller;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
@RequiredArgsConstructor
public class PostController {
    final PostService service;

    @PostMapping("/create")
    public ResponseEntity<Response> createPost(@RequestBody NewPostDto newPostDto, HttpServletRequest request){
        return service.createPost(newPostDto, request);
    }
}
