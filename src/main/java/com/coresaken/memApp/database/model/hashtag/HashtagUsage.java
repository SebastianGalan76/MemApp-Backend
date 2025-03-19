package com.coresaken.memApp.database.model.hashtag;

import com.coresaken.memApp.database.model.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashtagUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime usedAt = LocalDateTime.now();
}
