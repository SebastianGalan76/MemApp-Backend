package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.post.DeletePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeletePostController {
    final DeletePostService deletePostService;

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Response> deletePost(@PathVariable("id") Long id){
        return deletePostService.delete(id);
    }
}
