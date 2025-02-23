package com.coresaken.memApp.service.comment;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.comment.CommentRating;
import com.coresaken.memApp.database.repository.comment.CommentRatingRepository;
import com.coresaken.memApp.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentRatingService {
    final UserService userService;
    final CommentService commentService;

    final CommentRatingRepository commentRatingRepository;

    public ResponseEntity<Response> rate(Long commentId, byte ratingValue, HttpServletRequest request) {
        if(ratingValue != -1 && ratingValue != 1 && ratingValue != 0){
            return Response.badRequest(1, "Nieprawidłowa wartość oceny.");
        }

        Comment comment = commentService.findById(commentId).orElse(null);
        if(comment == null){
            return Response.badRequest(2, "Brak komentarza o podanym identyfikatorze. Możliwe, że komentarz został usunięty.");
        }

        User user = userService.getLoggedInUser();
        String userIP = request.getRemoteAddr();

        CommentRating commentRating = getUserRatingForComment(comment, user, userIP);
        if(ratingValue == 0){
            commentRatingRepository.delete(commentRating);
            return Response.ok("Usunięto prawidłowo ocenę");
        }
        else{
            commentRating.setComment(comment);
            commentRating.setUser(user);
            commentRating.setRatingValue(ratingValue);
            commentRating.setUserIp(userIP);

            commentRatingRepository.save(commentRating);
            return Response.ok("Oceniono prawidłowo komentarz");
        }
    }

    public CommentRating getUserRatingForComment(Comment comment, User user, String userIp){
        if(user != null){
            List<CommentRating> ratings = commentRatingRepository.findByCommentAndUser(comment, user);

            if(ratings != null && !ratings.isEmpty()){
                return ratings.getFirst();
            }
        }
        List<CommentRating> ratings = commentRatingRepository.findByCommentAndUserIp(comment, userIp);
        if(ratings != null && !ratings.isEmpty()){
            CommentRating commentRating = ratings.getFirst();
            if(commentRating.getUser() == null){
                return commentRating;
            }
        }
        return new CommentRating();
    }
}
