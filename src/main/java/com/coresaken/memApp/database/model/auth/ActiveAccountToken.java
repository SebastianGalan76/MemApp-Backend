package com.coresaken.memApp.database.model.auth;

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
@Table(name = "active_account_token")
public class ActiveAccountToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    User user;

    @Column(length = 36)
    String token;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime createdAt;

    public ActiveAccountToken(User user, String token){
        this.user = user;
        this.token = token;

        createdAt = LocalDateTime.now();
    }
}
