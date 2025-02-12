package com.coresaken.memApp.database.model.post;

import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserPostList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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

    private boolean contentSpoiler;
    private boolean contentNSFW;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostRating> postRatingList;

    @ManyToMany(mappedBy = "savedPosts")
    private List<UserPostList> userPostList = new ArrayList<>();

    public enum Type{
        IMAGE, TIKTOK, INSTAGRAM, X;
    }
    public enum Visibility{
        PUBLIC, NOT_PUBLIC, PRIVATE
    }
}
