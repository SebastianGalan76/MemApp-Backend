package com.coresaken.memApp.service.user;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    final UserService userService;
    final UserRepository userRepository;

    final PostRepository postRepository;

    @Transactional
    public ResponseEntity<Response> follow(Long userId) {
        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            return Response.badRequest(1, "Zaloguj się, aby obserwować użytkownika!");
        }

        Optional<User> followingOptional = userRepository.findById(userId);
        if(followingOptional.isEmpty()){
            return Response.badRequest(2, "Brak użytkownika z podanym identyfikatorem!");
        }

        User following = followingOptional.get();

        Optional<User> followerOptional = userRepository.findById(loggedInUser.getId());
        if(followerOptional.isEmpty()){
            return Response.badRequest(2, "Brak użytkownika z podanym identyfikatorem!");
        }

        User follower = followerOptional.get();

        if(follower.equals(following)){
            return Response.badRequest(3, "Nie możesz obserwować samego siebie!");
        }

        if(follower.getFollowing().contains(following)){
            follower.unfollow(following);
            userRepository.save(follower);
            return Response.ok("Przestałeś obserwować tego użytkownika.");
        }
        else{
            follower.follow(following);
            userRepository.save(follower);
            return Response.ok("Zacząłeś obserwować tego użytkownika.");
        }
    }

    public Page<PostDto> getPosts(int page, HttpServletRequest request) {
        if(page<0){
            return null;
        }

        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            return null;
        }

        User user = userRepository.findById(loggedInUser.getId()).orElse(null);
        if(user == null){
            return null;
        }

        String userIp = request.getRemoteAddr();

        if(user.getFollowing() == null){
            return null;
        }

        Pageable pageable = PageRequest.of(page, 15);
        Page<Post> posts = postRepository.findPostsByFollowingUsers(user.getFollowing().stream().toList(), pageable);

        List<PostDto> result = posts.getContent().stream().map(post -> PostDtoMapper.toDTO(post, user, userIp)).toList();

        return new PageImpl<>(result, pageable, posts.getTotalElements());
    }

    public Page<UserDto> getUsers(int page) {
        if(page<0){
            return null;
        }

        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            return null;
        }

        User user = userRepository.findById(loggedInUser.getId()).orElse(null);
        if(user == null){
            return null;
        }

        if(user.getFollowing() == null){
            return null;
        }

        List<UserDto> result = user.getFollowing().stream().map(u -> {
            UserDto userDto = new UserDto();
            userDto.setId(u.getId());
            userDto.setLogin(u.getLogin());
            userDto.setAvatar(u.getAvatar());
            return userDto;
        }).toList();

        Pageable pageable = PageRequest.of(page, 15);
        return new PageImpl<>(result, pageable,user.getFollowers().size());
    }
}
