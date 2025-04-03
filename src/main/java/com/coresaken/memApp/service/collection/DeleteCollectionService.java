package com.coresaken.memApp.service.collection;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.collection.UserCollection;
import com.coresaken.memApp.database.repository.collection.CollectionRepository;
import com.coresaken.memApp.service.user.UserService;
import com.coresaken.memApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCollectionService {
    final UserService userService;

    final CollectionRepository collectionRepository;

    public ResponseEntity<Response> delete(Long collectionId) {
        User user = userService.getLoggedInUser();
        if(user == null){
            return Response.badRequest(1, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        UserCollection collection = collectionRepository.findById(collectionId).orElse(null);
        if(collection == null){
            return Response.badRequest(2, "Brak kolekcji do usunięcia. Prawdopodobnie została już usunięta.");
        }

        if(!collection.getOwner().equals(user) && !PermissionChecker.hasPermission(user.getRole(), User.Role.MODERATOR)){
            return Response.badRequest(3, "Nie masz wymaganych uprawnień, aby to zrobić!");
        }

        collectionRepository.delete(collection);
        return Response.ok("Kolekcja została prawidłowo usunięta.");
    }
}
