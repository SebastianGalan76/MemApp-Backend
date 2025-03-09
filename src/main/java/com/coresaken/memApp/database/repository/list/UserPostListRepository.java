package com.coresaken.memApp.database.repository.list;

import com.coresaken.memApp.database.model.list.UserPostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPostListRepository extends JpaRepository<UserPostList, Long> {
    Optional<UserPostList> findByUuid(UUID uuid);
}
