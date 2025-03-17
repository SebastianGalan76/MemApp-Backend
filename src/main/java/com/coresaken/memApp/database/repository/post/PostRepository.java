package com.coresaken.memApp.database.repository.post;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByScoreGreaterThanEqualOrderByScoreDesc(int score, Pageable pageable);
    Page<Post> findByScoreOrderByCreatedAtDesc(int score, Pageable pageable);
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Post> findByScoreGreaterThanEqual(int score);
}
