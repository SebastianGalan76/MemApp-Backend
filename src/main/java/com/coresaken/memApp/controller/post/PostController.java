package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.post.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {
    final PostService service;
    final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/post/create")
    public ResponseEntity<Response> createPost(
            @RequestParam("newPostDto") String newPostDtoJson,
            @RequestParam("contentFile") MultipartFile contentFile,
            HttpServletRequest request) throws JsonProcessingException {
        return service.createPost(objectMapper.readValue(newPostDtoJson, NewPostDto.class), contentFile, request);
    }
}
