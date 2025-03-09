package com.coresaken.memApp.service.post;

import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostScoreService {
    private static final long EPOCH = LocalDateTime.of(2025, 1, 1, 0, 0)
            .toEpochSecond(ZoneOffset.UTC);

    final PostRepository postRepository;

    public int calculateScore(Post post) {
        long likes = post.getLikeAmount();
        long dislikes = post.getDislikeAmount();
        long comments = post.getCommentAmount();

        double scores = calculateHotScore(likes, dislikes, comments, post.getCreatedAt());
        return (int)(scores * 10000);
    }

    @Scheduled(fixedRate = 900000)
    public void updateScores() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            post.setScore(calculateScore(post));
            postRepository.save(post);
        }
    }

    public double calculateHotScore(long likes, long dislikes, long comments, LocalDateTime createdAt) {
        long postTime = createdAt.toEpochSecond(ZoneOffset.UTC);
        long ageInSeconds = postTime - EPOCH;

        long score = likes - dislikes;
        double logScore = (score > 0) ? Math.log10(score) : 0;
        double timeFactor = (double) ageInSeconds / 45000.0;

        return logScore + (comments * 0.5 / 10) + timeFactor;
    }
}
