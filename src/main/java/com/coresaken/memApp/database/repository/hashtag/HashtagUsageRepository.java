package com.coresaken.memApp.database.repository.hashtag;

import com.coresaken.memApp.database.model.hashtag.HashtagUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HashtagUsageRepository extends JpaRepository<HashtagUsage, Long> {
    @Query("SELECT h, COUNT(h) FROM HashtagUsage hu " +
            "JOIN hu.hashtag h " +
            "WHERE hu.usedAt >= :since " +
            "GROUP BY h ORDER BY COUNT(h) DESC")
    List<Object[]> findPopularHashtags(LocalDateTime since);
}
