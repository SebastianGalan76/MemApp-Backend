package com.coresaken.memApp.controller.collection;

import com.coresaken.memApp.data.dto.UserCollectionDto;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.collection.UserCollection;
import com.coresaken.memApp.service.list.UserPostListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserCollectionController {
    final UserPostListService service;

    @PostMapping("/collection/create")
    public ResponseEntity<ObjectResponse<UserCollection>> createCollection(@RequestParam("name") String name, @RequestParam("accessibility") String accessibility){
        return service.create(name, accessibility);
    }

    @PostMapping("/collection/save/{postID}/{listID}")
    public ResponseEntity<Response> savePost(@PathVariable("postID") Long postID, @PathVariable("listID") Long listID){
        return service.save(postID, listID);
    }

    @GetMapping("/collection/{uuid}/{page}")
    public ResponseEntity<ObjectResponse<UserCollectionDto>> getUserCollection(@PathVariable("uuid") String uuid, @PathVariable("page") int page, HttpServletRequest request){
        return service.getCollection(uuid, page, request);
    }
}
