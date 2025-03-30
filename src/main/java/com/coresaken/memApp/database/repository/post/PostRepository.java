package com.coresaken.memApp.database.repository.post;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByScoreGreaterThanEqualOrderByScoreDesc(int score, Pageable pageable);
    Page<Post> findByScoreOrderByCreatedAtDesc(int score, Pageable pageable);
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("SELECT p FROM HashtagUsage hu JOIN hu.post p JOIN hu.hashtag h WHERE h.tag = :tag")
    Page<Post> findPostsByHashtag(@Param("tag") String tag, Pageable pageable);

    List<Post> findByScoreGreaterThanEqual(int score);

    @Query("SELECT p FROM Post p WHERE p.user IN :followingUsers ORDER BY p.id DESC")
    Page<Post> findPostsByFollowingUsers(List<User> followingUsers, Pageable pageable);
}
