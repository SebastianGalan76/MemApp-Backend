package com.coresaken.memApp.service.user;

import com.coresaken.memApp.database.model.User;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Nullable
    public User getLoggedInUser(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal() instanceof User userDetails ? userDetails : null;
    }
}
