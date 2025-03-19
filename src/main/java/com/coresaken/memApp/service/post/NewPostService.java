package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.dto.NewPostDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.hashtag.HashtagService;
import com.coresaken.memApp.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewPostService {
    final UserService userService;
    final PostFileService postFileService;
    final PostScoreService postScoreService;

    final PostFlagService postFlagService;
    final HashtagService hashtagService;

    final PostRepository postRepository;

    public ResponseEntity<Response> createPost(NewPostDto newPostDto, MultipartFile contentFile, HttpServletRequest request) {
        if(newPostDto.text() != null && newPostDto.text().length() > 255){
            return Response.badRequest(1, "Tekst postu jest zbyt długi");
        }
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setUuid(UUID.randomUUID());
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
                post.setContent(newPostDto.content());
            }
        }
        else{
            if (post.getType() == Post.Type.TIKTOK){
                post.setContent(newPostDto.content());
            }
        }

        post.setUser(userService.getLoggedInUser());
        post.setCreatorIp(request.getRemoteAddr());

        post.setCreatedAt(now);
        post.setScore(-1);

        postRepository.save(post);

        postFlagService.setPostFlag(post, newPostDto.flags());
        hashtagService.setPostHashtag(post, newPostDto.hashtags());

        return Response.ok("Stworzono prawidłowo nowy post");
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}
