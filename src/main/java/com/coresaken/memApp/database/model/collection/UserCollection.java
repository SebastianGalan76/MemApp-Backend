package com.coresaken.memApp.database.model.collection;

import com.coresaken.memApp.database.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    @Column(length = 30)
    private String name;

    @Column(length = 6)
    private String color;

    @Enumerated(EnumType.STRING)
    Accessibility accessibility;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User owner;

    @OneToMany(mappedBy = "userCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<UserCollectionPost> savedPosts = new ArrayList<>();


    public enum Accessibility{
        PUBLIC,  NOT_PUBLIC, PRIVATE;
    }
}


