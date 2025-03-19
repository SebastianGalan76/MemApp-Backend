package com.coresaken.memApp.service.hashtag;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.database.model.hashtag.Hashtag;
import com.coresaken.memApp.database.model.hashtag.HashtagUsage;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.hashtag.HashtagRepository;
import com.coresaken.memApp.database.repository.hashtag.HashtagUsageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class HashtagService {
    final HashtagRepository hashtagRepository;
    final HashtagUsageRepository hashtagUsageRepository;

    List<HashtagDto> popularHashtags = new ArrayList<>();
    LocalDateTime refreshedAt;

    public void setPostHashtag(Post post, Set<String> hashtags){
        hashtags = hashtags.stream().map(String::toLowerCase).collect(Collectors.toSet());

        hashtags.forEach(tag -> {
            Hashtag hashtag = hashtagRepository.findByTag(tag)
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setTag(tag);
                        return hashtagRepository.save(newHashtag);
                    });

            HashtagUsage usage = new HashtagUsage();
            usage.setPost(post);
            usage.setHashtag(hashtag);
            hashtagUsageRepository.save(usage);
        });
    }

    public ResponseEntity<ObjectResponse<PopularHashtagDto>> getPopularHashtags(){
        return ObjectResponse.ok("", new PopularHashtagDto(popularHashtags, refreshedAt));
    }

    @Scheduled(fixedRate = 900000)
    public void refreshPopularHashtags() {
        popularHashtags =  hashtagUsageRepository.findPopularHashtags(LocalDateTime.now().minusHours(240)).stream()
                .map(result -> new HashtagDto((Hashtag) result[0], ((Long) result[1]).intValue()))
                .collect(Collectors.toList());
        refreshedAt = LocalDateTime.now();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HashtagDto{
        Hashtag hashtag;
        int popularity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PopularHashtagDto{
        List<HashtagDto> hashtags;
        LocalDateTime refreshedAt;
    }
}
