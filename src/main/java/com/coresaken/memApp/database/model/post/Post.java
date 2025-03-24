package com.coresaken.memApp.database.model.post;

import com.coresaken.memApp.database.model.Report;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.hashtag.HashtagUsage;
import com.coresaken.memApp.database.model.collection.UserCollectionPost;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Nullable
    private User user;

    @Column(length = 15)
    private String creatorIp;

    private String text;

    @Column(length = 2048)
    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private LocalDateTime createdAt;

    private int commentAmount;
    private int likeAmount;
    private int dislikeAmount;

    private int score;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostRating> postRatingList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCollectionPost> userCollectionPosts;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostFlag> flags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HashtagUsage> hashtags;

    @OneToMany(mappedBy = "reportedPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportList;

    public enum Type{
        IMAGE, TIKTOK, INSTAGRAM, X;
    }
    public enum Visibility{
        PUBLIC, NOT_PUBLIC, PRIVATE
    }
}
