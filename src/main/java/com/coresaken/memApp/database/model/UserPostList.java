package com.coresaken.memApp.database.model;

import com.coresaken.memApp.database.model.post.Post;
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
public class UserPostList {
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

    @ManyToMany(cascade = {CascadeType.ALL})
            @JoinTable(
                    name = "UserPostList_Post",
                    joinColumns = {@JoinColumn(name = "user_post_list_id")},
                    inverseJoinColumns = {@JoinColumn(name = "post_id")}
            )
    @JsonIgnore
    @ToString.Exclude
    Set<Post> savedPosts = new HashSet<>();


    public enum Accessibility{
        PUBLIC,  NOT_PUBLIC, PRIVATE;
    }
}


