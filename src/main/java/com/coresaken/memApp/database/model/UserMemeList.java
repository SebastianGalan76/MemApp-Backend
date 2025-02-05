package com.coresaken.memApp.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMemeList {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30)
    String name;

    @Column(length = 6)
    String color;

    @Enumerated(EnumType.STRING)
    Accessibility accessibility;

    UUID uuid;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    User owner;

    public enum Accessibility{
        PUBLIC,  NON_PUBLIC, PRIVATE;
    }
}


