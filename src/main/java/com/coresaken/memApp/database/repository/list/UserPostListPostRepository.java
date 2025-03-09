package com.coresaken.memApp.database.repository.list;

import com.coresaken.memApp.database.model.list.UserPostList;
import com.coresaken.memApp.database.model.list.UserPostListPost;
import com.coresaken.memApp.database.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostListPostRepository extends JpaRepository<UserPostListPost, Long> {
    @Query("SELECT uplp FROM UserPostListPost uplp WHERE uplp.userPostList = :userPostList AND uplp.post = :post")
    UserPostListPost findByUserPostListAndPost(@Param("userPostList") UserPostList userPostList, @Param("post") Post post);

    Page<UserPostListPost> findByUserPostListOrderByAddedAtDesc(UserPostList userPostList, Pageable pageable);
}
