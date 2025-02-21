package com.coresaken.memApp.controller.comment;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.comment.CommentRatingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentRatingController {
    final CommentRatingService commentRatingService;

    @PostMapping("/comment/rate")
    public ResponseEntity<Response> ratePost(@RequestParam("comment_id") Long postID, @RequestParam("rating_value") byte ratingValue, HttpServletRequest request){
        return commentRatingService.rate(postID, ratingValue, request);
    }
}
