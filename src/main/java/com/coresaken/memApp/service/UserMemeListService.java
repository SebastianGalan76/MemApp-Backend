package com.coresaken.memApp.service;

import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserMemeList;
import com.coresaken.memApp.database.repository.UserMemeListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMemeListService {
    final UserMemeListRepository repository;

    final UserService userService;

    public ResponseEntity<ObjectResponse<UserMemeList>> createList(String name, String accessibility) {
        if(name == null || name.isBlank()){
            return ObjectResponse.badRequest(1, "Nazwa nie może być pusta!");
        }
        name = name.trim();

        UserMemeList.Accessibility accessibilityType;
        try{
            accessibilityType = UserMemeList.Accessibility.valueOf(accessibility);
        }catch (IllegalArgumentException e){
            accessibilityType = UserMemeList.Accessibility.PRIVATE;
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return ObjectResponse.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        UserMemeList userMemeList = new UserMemeList();
        userMemeList.setName(name);
        userMemeList.setAccessibility(accessibilityType);
        userMemeList.setUuid(UUID.randomUUID());
        userMemeList.setOwner(user);

        userMemeList = repository.save(userMemeList);
        return ObjectResponse.ok("Stworzono poprawnie nową listę.", userMemeList);
    }
}
