package com.coresaken.memApp.controller;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.database.model.UserMemeList;
import com.coresaken.memApp.service.UserMemeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserMemeListController {
    final UserMemeListService service;

    @PostMapping("/user-meme-list/create")
    public ResponseEntity<ObjectResponse<UserMemeList>> createList(@RequestParam("name") String name, @RequestParam("accessibility") String accessibility){
        return service.createList(name, accessibility);
    }
}
