package com.coresaken.memApp.database.repository;

import com.coresaken.memApp.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrLogin(String email, String login);

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
}
