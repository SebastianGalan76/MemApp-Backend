package com.coresaken.memApp.database.model;

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
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(length = 15)
    String creatorIP;

    String text;
    String content;

    @Enumerated(EnumType.STRING)
    Type type;
    @Enumerated(EnumType.STRING)
    Visibility visibility;

    boolean contentSpoiler;
    boolean contentNSFW;

    LocalDateTime createdAt;


    public enum Type{
        PHOTO, EMBEDDED_TIKTOK, EMBEDDED_INSTAGRAM, EMBEDDED_X;
    }
    public enum Visibility{
        PUBLIC, NOT_PUBLIC, PRIVATE
    }
}
