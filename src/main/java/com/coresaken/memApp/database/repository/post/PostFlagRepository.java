package com.coresaken.memApp.database.repository.post;

import com.coresaken.memApp.database.model.post.PostFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFlagRepository extends JpaRepository<PostFlag, Long> {
}
