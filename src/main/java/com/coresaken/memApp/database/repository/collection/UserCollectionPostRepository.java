package com.coresaken.memApp.database.repository.collection;

import com.coresaken.memApp.database.model.collection.UserCollection;
import com.coresaken.memApp.database.model.collection.UserCollectionPost;
import com.coresaken.memApp.database.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectionPostRepository extends JpaRepository<UserCollectionPost, Long> {
    @Query("SELECT uplp FROM UserCollectionPost uplp WHERE uplp.userCollection = :userCollection AND uplp.post = :post")
    UserCollectionPost findByUserCollectionAndPost(@Param("userCollection") UserCollection userCollection, @Param("post") Post post);

    Page<UserCollectionPost> findByUserCollectionOrderByAddedAtDesc(UserCollection userCollection, Pageable pageable);
}
