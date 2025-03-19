package com.coresaken.memApp.controller.hashtag;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.service.hashtag.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class HashtagController {
    final HashtagService hashtagService;

    @GetMapping("/hashtag/popular")
    public ResponseEntity<ObjectResponse<HashtagService.PopularHashtagDto>> getPopularHashtags(){
        return hashtagService.getPopularHashtags();
    }
}
