package com.coresaken.memApp.controller.user;

import com.coresaken.memApp.service.user.UserAvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAvatarController {
    final UserAvatarService userAvatarService;

    @GetMapping("/uploads/user/avatar/{filename:.+}")
    public ResponseEntity<Resource> get(@PathVariable String filename) {
        return userAvatarService.getFile(filename);
    }
}
