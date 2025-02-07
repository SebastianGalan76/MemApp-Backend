package com.coresaken.memApp.service;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.Post;
import com.coresaken.memApp.database.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    final UserService userService;

    final PostRepository repository;

    public ResponseEntity<Response> createPost(NewPostDto newPostDto, HttpServletRequest request) {
        if(newPostDto.text() != null && newPostDto.text().length() > 255){
            return Response.badRequest(1, "Tekst postu jest zbyt długi");
        }

        Post post = new Post();
        post.setText(newPostDto.text());
        post.setContent(newPostDto.content());

        post.setVisibility(newPostDto.visibility());
        post.setContentNSFW(newPostDto.contentNSFW());
        post.setContentSpoiler(newPostDto.contentSpoiler());
        post.setContentNSFW(newPostDto.contentNSFW());

        post.setUser(userService.getLoggedInUser());
        post.setCreatorIP(request.getRemoteAddr());

        repository.save(post);
        return Response.ok("Stworzono prawidłowo nowy post");
    }

    public Optional<Post> findById(Long postId) {
        return repository.findById(postId);
    }
}
