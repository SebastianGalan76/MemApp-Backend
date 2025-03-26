package com.coresaken.memApp.service.user;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    final UserService userService;
    final UserRepository userRepository;

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
}
