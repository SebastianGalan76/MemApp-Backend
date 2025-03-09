package com.coresaken.memApp.controller.list;

import com.coresaken.memApp.data.dto.UserListDto;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.list.UserPostList;
import com.coresaken.memApp.service.list.UserPostListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserPostListController {
    final UserPostListService service;

    @PostMapping("/user-post-list/create")
    public ResponseEntity<ObjectResponse<UserPostList>> createList(@RequestParam("name") String name, @RequestParam("accessibility") String accessibility){
        return service.create(name, accessibility);
    }

    @PostMapping("/user-post-list/save/{postID}/{listID}")
    public ResponseEntity<Response> savePost(@PathVariable("postID") Long postID, @PathVariable("listID") Long listID){
        return service.save(postID, listID);
    }

    @GetMapping("/user-post-list/{uuid}/{page}")
    public ResponseEntity<ObjectResponse<UserListDto>>  getUserList(@PathVariable("uuid") String uuid, @PathVariable("page") int page, HttpServletRequest request){
        return service.getList(uuid, page, request);
    }
}
