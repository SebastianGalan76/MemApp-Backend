package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.post.NewPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class NewPostController {
    final NewPostService newPostservice;

    final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/post/create")
    public ResponseEntity<Response> createPost(
            @RequestParam("newPostDto") String newPostDtoJson,
            @RequestParam(value = "contentFile", required = false) MultipartFile contentFile,
            HttpServletRequest request) throws JsonProcessingException {
        return newPostservice.createPost(objectMapper.readValue(newPostDtoJson, NewPostDto.class), contentFile, request);
    }
}
