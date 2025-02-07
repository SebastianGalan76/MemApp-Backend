package com.coresaken.memApp.database.model.post;

import com.coresaken.memApp.database.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private boolean contentSpoiler;
    private boolean contentNSFW;

    private LocalDateTime createdAt;

    public enum Type{
        PHOTO, EMBEDDED_TIKTOK, EMBEDDED_INSTAGRAM, EMBEDDED_X;
    }
    public enum Visibility{
        PUBLIC, NOT_PUBLIC, PRIVATE
    }
}
