package com.coresaken.memApp.controller.post;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.post.PostRatingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostRatingController {
    final PostRatingService service;

    @PostMapping("/post/rate")
    public ResponseEntity<Response> ratePost(@RequestParam("post_id") Long postId, @RequestParam("rating_value") byte ratingValue, HttpServletRequest request){
        return service.rate(postId, ratingValue, request);
    }
}
