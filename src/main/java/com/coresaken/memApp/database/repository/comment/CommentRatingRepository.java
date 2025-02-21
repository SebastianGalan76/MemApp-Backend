package com.coresaken.memApp.database.repository.comment;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.comment.CommentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRatingRepository extends JpaRepository<CommentRating, Long> {
    List<CommentRating> findByCommentAndUser(Comment comment, User user);
    List<CommentRating> findByCommentAndUserIp(Comment comment, String userIp);
}
