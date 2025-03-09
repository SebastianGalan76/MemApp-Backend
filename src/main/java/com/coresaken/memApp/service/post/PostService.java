package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    final UserService userService;

    final PostRepository postRepository;

    public Page<PostDto> getHomePosts(int page, HttpServletRequest request) {
        if(page<0){
            return null;
        }

        User user = userService.getLoggedInUser();
        String userIp = request.getRemoteAddr();

        Pageable pageable = PageRequest.of(page, 15);
        Page<Post> posts = postRepository.findAllByOrderByScoreDesc(pageable);

        List<PostDto> result = posts.getContent().stream().map(post -> PostDtoMapper.toDTO(post, user, userIp)).toList();

        return new PageImpl<>(result, pageable, posts.getTotalElements());
    }

    public ResponseEntity<PostDto> getPostDto(Long id, HttpServletRequest request) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(PostDtoMapper.toDTO(postOptional.get(), userService.getLoggedInUser(), request.getRemoteAddr()));
    }
}
