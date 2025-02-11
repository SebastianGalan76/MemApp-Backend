package com.coresaken.memApp.database.repository;

import com.coresaken.memApp.database.model.UserPostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostListRepository extends JpaRepository<UserPostList, Long> {
}
