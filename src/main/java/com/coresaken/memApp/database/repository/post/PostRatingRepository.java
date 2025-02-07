package com.coresaken.memApp.database.repository.post;

import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import com.coresaken.memApp.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRatingRepository extends JpaRepository<PostRating, Long> {
    List<PostRating> findByPostAndUser(Post post, User user);
    List<PostRating> findByPostAndUserIp(Post post, String userIp);
}
