package com.coresaken.memApp.database.repository;

import com.coresaken.memApp.database.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrLogin(String email, String login);

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "JOIN u.following f " +
            "WHERE f.follower = :follower " +
            "ORDER BY f.followedAt ASC")
    Page<User> findFollowingSortedByFollowedAtAsc(@Param("follower") User follower, Pageable pageable);

    // Sortowanie po followedAt malejąco
    @Query("SELECT u FROM User u " +
            "JOIN u.following f " +
            "WHERE f.follower = :follower " +
            "ORDER BY f.followedAt DESC")
    Page<User> findFollowingSortedByFollowedAtDesc(@Param("follower") User follower, Pageable pageable);

    // Sortowanie po loginie
    @Query("SELECT u FROM User u " +
            "JOIN u.following f " +
            "WHERE f.follower = :follower " +
            "ORDER BY u.login ASC")
    Page<User> findFollowingSortedByLoginAsc(@Param("follower") User follower, Pageable pageable);

    // Sortowanie po loginie malejąco
    @Query("SELECT u FROM User u " +
            "JOIN u.following f " +
            "WHERE f.follower = :follower " +
            "ORDER BY u.login DESC")
    Page<User> findFollowingSortedByLoginDesc(@Param("follower") User follower, Pageable pageable);
}
