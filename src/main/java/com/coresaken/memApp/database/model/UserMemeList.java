package com.coresaken.memApp.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMemeList {
    @Id
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
    @JsonIgnore
    @ToString.Exclude
    User owner;

    public enum Accessibility{
        PUBLIC,  NOT_PUBLIC, PRIVATE;
    }
}


