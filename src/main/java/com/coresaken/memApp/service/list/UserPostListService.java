package com.coresaken.memApp.service.list;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserPostList;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.UserPostListRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPostListService {
    final UserService userService;

    final UserPostListRepository userPostListRepository;
    final PostRepository postRepository;

    public ResponseEntity<ObjectResponse<UserPostList>> create(String name, String accessibility) {
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

        userPostList = userPostListRepository.save(userPostList);
        return ObjectResponse.ok("Stworzono poprawnie nową listę.", userPostList);
    }

    public ResponseEntity<Response> save(Long postID, Long listID) {
        UserPostList postList = userPostListRepository.findById(listID).orElse(null);
        if(postList == null){
            return Response.badRequest(1, "Nie znaleziono listy o podanym ID. Lista została prawdopodobnie usunięta!");
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return Response.badRequest(2, "Musisz się zalogować, aby dodać post do swojej listy!");
        }

        if(!postList.getOwner().equals(user)){
            return Response.badRequest(3, "Nie możesz dodać postu do tej listy!");
        }

        Post post = postRepository.findById(postID).orElse(null);
        if(post == null){
            return Response.badRequest(4, "Nie znaleziono postu o podanym ID. Post został prawdopodobnie usunięty!");
        }

        if(postList.getSavedPosts().contains(post)){
            postList.getSavedPosts().remove(post);
        }
        else{
            postList.getSavedPosts().add(post);
        }

        userPostListRepository.save(postList);
        return Response.ok("Dodano post do listy");
    }
}
