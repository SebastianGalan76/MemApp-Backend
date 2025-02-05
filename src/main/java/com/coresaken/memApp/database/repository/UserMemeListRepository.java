package com.coresaken.memApp.database.repository;

import com.coresaken.memApp.database.model.UserMemeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemeListRepository extends JpaRepository<UserMemeList, Long> {
}
