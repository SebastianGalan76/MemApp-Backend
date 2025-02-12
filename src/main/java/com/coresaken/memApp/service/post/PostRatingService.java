package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.repository.post.PostRatingRepository;
import com.coresaken.memApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRatingService {
    final UserService userService;
    final NewPostService newPostService;

    final PostRatingRepository repository;

    public ResponseEntity<Response> rate(Long postId, byte ratingValue, HttpServletRequest request) {
        if(ratingValue != -1 && ratingValue != 1 && ratingValue != 0){
            return Response.badRequest(2, "Nieprawidłowa wartość oceny.");
        }

        Post post = newPostService.findById(postId).orElse(null);
        if(post == null){
            return Response.badRequest(1, "Brak postu o podanym identyfikatorze. Możliwe, że post został usunięty.");
        }

        User user = userService.getLoggedInUser();
        String userIp = request.getRemoteAddr();

        PostRating postRating = getUserRatingForPost(post, user, userIp);
        if(ratingValue == 0){
            repository.delete(postRating);
            return Response.ok("Usunięto prawidłowo ocenę");
        }
        else{
            postRating.setPost(post);
            postRating.setUser(user);
            postRating.setRatingValue(ratingValue);
            postRating.setUserIp(userIp);

            repository.save(postRating);
            return Response.ok("Oceniono prawidłowo post");
        }
    }

    public PostRating getUserRatingForPost(Post post, User user, String userIp){
        if(user != null){
            List<PostRating> ratings = repository.findByPostAndUser(post, user);

            if(ratings != null && !ratings.isEmpty()){
                return ratings.getFirst();
            }
        }
        List<PostRating> ratings = repository.findByPostAndUserIp(post, userIp);
        if(ratings != null && !ratings.isEmpty()){
            PostRating postRating = ratings.getFirst();
            if(postRating.getUser() == null){
                return postRating;
            }
        }
        return new PostRating();
    }
}
