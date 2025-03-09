package com.coresaken.memApp.database.model.list;

import com.coresaken.memApp.database.model.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostListPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_post_list_id", nullable = false)
    UserPostList userPostList;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    LocalDateTime addedAt;
}
