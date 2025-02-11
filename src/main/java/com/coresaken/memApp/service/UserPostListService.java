package com.coresaken.memApp.service;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserPostList;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.UserPostListRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPostListService {
    final UserPostListRepository repository;
    final PostRepository postRepository;

    final UserService userService;

    public ResponseEntity<ObjectResponse<UserPostList>> createList(String name, String accessibility) {
        if(name == null || name.isBlank()){
            return ObjectResponse.badRequest(1, "Nazwa nie może być pusta!");
        }
        name = name.trim();

        UserPostList.Accessibility accessibilityType;
        try{
            accessibilityType = UserPostList.Accessibility.valueOf(accessibility);
        }catch (IllegalArgumentException e){
            accessibilityType = UserPostList.Accessibility.PRIVATE;
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return ObjectResponse.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        UserPostList userPostList = new UserPostList();
        userPostList.setName(name);
        userPostList.setAccessibility(accessibilityType);
        userPostList.setUuid(UUID.randomUUID());
        userPostList.setOwner(user);

        userPostList = repository.save(userPostList);
        return ObjectResponse.ok("Stworzono poprawnie nową listę.", userPostList);
    }

    public ResponseEntity<Response> save(Long postID, Long listID) {
        UserPostList postList = repository.findById(listID).orElse(null);
        if(postList == null){
            return Response.badRequest(1, "");
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return Response.badRequest(2, "");
        }

        if(!postList.getOwner().equals(user)){
            return Response.badRequest(3, "");
        }

        Post post = postRepository.findById(postID).orElse(null);
        if(post == null){
            return Response.badRequest(4, "");
        }

        if(postList.getSavedPosts().contains(post)){
            postList.getSavedPosts().remove(post);
        }
        else{
            postList.getSavedPosts().add(post);
        }

        repository.save(postList);
        return Response.ok("Dodano post do listy");
    }
}
