package com.coresaken.memApp.controller.comment;

import com.coresaken.memApp.data.dto.CommentDto;
import com.coresaken.memApp.data.dto.NewCommentDto;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.comment.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CommentController {
    final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ObjectResponse<CommentDto>> comment(@RequestBody NewCommentDto newCommentDto){
        return commentService.comment(newCommentDto);
    }

    @GetMapping("/comment/post/{id}/{page}")
    public Page<CommentDto> getCommentsForPost(@PathVariable("id") Long id, @PathVariable("page") int page, HttpServletRequest request){
        return commentService.getCommentsForPost(id, page, request);
    }

    @GetMapping("/comment/parent/{id}/{page}")
    public Page<CommentDto> getCommentsForComment(@PathVariable("id") Long id, @PathVariable("page") int page, HttpServletRequest request){
        return commentService.getCommentsForComment(id, page, request);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id){
        return commentService.delete(id);
    }
}
