package com.coresaken.memApp.controller;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.database.model.UserPostList;
import com.coresaken.memApp.service.UserPostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserPostListController {
    final UserPostListService service;

    @PostMapping("/user-post-list/create")
    public ResponseEntity<ObjectResponse<UserPostList>> createList(@RequestParam("name") String name, @RequestParam("accessibility") String accessibility){
        return service.createList(name, accessibility);
    }
}
