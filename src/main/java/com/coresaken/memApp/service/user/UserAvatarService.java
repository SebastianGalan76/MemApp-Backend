package com.coresaken.memApp.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserAvatarService {
    final String[] COLORS = {"#628ad9", "#6a62d9", "#8e62d9", "#bd62d9", "#d962af", "#62b9d9", "#62d9cc", "#62d997", "#6ad962", "#a5d962", "#d7d962", "#d9b662"};

    final UserAvatarFileService profilePictureFileService;
    final Random random = new Random();

    public ResponseEntity<Resource> getFile(String filename) {
        return profilePictureFileService.get(filename);
    }

    public String getRandomColor(){
        return COLORS[random.nextInt(COLORS.length)];
    }
}
