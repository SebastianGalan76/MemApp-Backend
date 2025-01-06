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
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Transient
    int TOKEN_EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    User user;

    String token;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime expiredAt;

    public ResetPasswordToken(User user, String token){
        this.user = user;
        this.token = token;

        expiredAt = LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME);
    }

    public void setToken(String token){
        this.token = token;
        expiredAt = LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME);
    }
}
