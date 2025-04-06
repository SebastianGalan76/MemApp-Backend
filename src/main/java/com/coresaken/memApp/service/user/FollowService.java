package com.coresaken.memApp.service.user;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserFollowRelation;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<User> followerOptional = userRepository.findById(loggedInUser.getId());
        if(followerOptional.isEmpty()){
            return Response.badRequest(2, "Brak użytkownika z podanym identyfikatorem!");
        }
        User follower = followerOptional.get();

        Optional<User> followingOptional = userRepository.findById(userId);
        if(followingOptional.isEmpty()){
            return Response.badRequest(2, "Brak użytkownika z podanym identyfikatorem!");
        }

        User following = followingOptional.get();

        if(follower.equals(following)){
            return Response.badRequest(3, "Nie możesz obserwować samego siebie!");
        }

        boolean isAlreadyFollowing = follower.getFollowers().stream()
                .anyMatch(ufr -> ufr.getFollowing().equals(following));

        if(isAlreadyFollowing){
            following.getFollowing().removeIf(ufr -> ufr.getFollower().equals(follower));

            userRepository.save(following);
            return Response.ok("Przestałeś obserwować tego użytkownika.");
        }
        else{
            UserFollowRelation userFollowRelation = new UserFollowRelation();
            userFollowRelation.setFollowing(following);
            userFollowRelation.setFollower(follower);

            follower.getFollowing().add(userFollowRelation);
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
        Page<Post> posts = postRepository.findPostsByFollowingUsers(user.getFollowers().stream().map(UserFollowRelation::getFollowing).toList(), pageable);

        List<PostDto> result = posts.getContent().stream().map(post -> PostDtoMapper.toDTO(post, user, userIp)).toList();

        return new PageImpl<>(result, pageable, posts.getTotalElements());
    }

    public Page<UserDto> getUsers(int page, String sortBy, String order) {
        if(page<0){
            return null;
        }

        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            return null;
        }

        Pageable pageable = PageRequest.of(page, 15);
        Page<User> userPage = null;

        if ("followedAt".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(order)) {
                userPage = userRepository.findFollowingSortedByFollowedAtDesc(loggedInUser, pageable);
            } else {
                userPage = userRepository.findFollowingSortedByFollowedAtAsc(loggedInUser, pageable);
            }
        }
        else if ("login".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(order)) {
                userPage = userRepository.findFollowingSortedByLoginDesc(loggedInUser, pageable);
            } else {
                userPage = userRepository.findFollowingSortedByLoginAsc(loggedInUser, pageable);
            }
        }

        if (userPage == null) {
            return Page.empty();
        }

        List<UserDto> userDtoList = userPage.stream().map(u -> {
            UserDto userDto = new UserDto();
            userDto.setId(u.getId());
            userDto.setLogin(u.getLogin());
            userDto.setAvatar(u.getAvatar());
            return userDto;
        }).collect(Collectors.toList());

        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }
}
