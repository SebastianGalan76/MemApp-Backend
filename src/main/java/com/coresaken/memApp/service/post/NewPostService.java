package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewPostService {
    final UserService userService;
    final PostFileService postFileService;

    final PostRepository repository;

    public ResponseEntity<Response> createPost(NewPostDto newPostDto, MultipartFile contentFile, HttpServletRequest request) {
        if(newPostDto.text() != null && newPostDto.text().length() > 255){
            return Response.badRequest(1, "Tekst postu jest zbyt długi");
        }
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setText(newPostDto.text());

        post.setVisibility(newPostDto.visibility());
        post.setType(newPostDto.type());

        if(post.getType() == Post.Type.IMAGE){
            if(contentFile != null){
                ResponseEntity<Response> uploadResponse = postFileService.upload(now, contentFile);
                if(uploadResponse.getStatusCode() != HttpStatusCode.valueOf(200)){
                    return uploadResponse;
                }

                post.setContent(uploadResponse.getBody().getMessage());
            }
            else{
                //TODO Download and save file
            }
        }

        post.setUser(userService.getLoggedInUser());
        post.setCreatorIp(request.getRemoteAddr());

        post.setCreatedAt(now);

        repository.save(post);
        return Response.ok("Stworzono prawidłowo nowy post");
    }

    public Optional<Post> findById(Long postId) {
        return repository.findById(postId);
    }
}
