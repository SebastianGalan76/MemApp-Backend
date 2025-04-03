package com.coresaken.memApp.database.model.collection;

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
public class UserCollectionPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_collection_id", nullable = false)
    UserCollection userCollection;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    LocalDateTime addedAt = LocalDateTime.now();
}
