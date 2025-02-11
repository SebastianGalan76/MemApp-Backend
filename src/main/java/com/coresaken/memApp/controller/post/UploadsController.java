package com.coresaken.memApp.controller.post;


import com.coresaken.memApp.service.post.PostFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UploadsController {
    final PostFileService postFileService;

    @GetMapping("/uploads/post/{year}/{month}/{day}/{filename:.+}")
    public ResponseEntity<Resource> get(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String day,
            @PathVariable String filename) {
        return postFileService.get(year+"/"+month+"/"+day+"/"+filename);
    }

}
