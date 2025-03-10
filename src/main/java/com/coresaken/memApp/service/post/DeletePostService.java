package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import com.coresaken.memApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletePostService {
    final UserService userService;

    final PostRepository postRepository;

    public ResponseEntity<Response> delete(Long id) {
        User user = userService.getLoggedInUser();

        if(user == null){
            return Response.badRequest(1, "Twoja sesja wygasła. Zaloguj się ponownie.");
        }

        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()){
            return Response.badRequest(2, "Brak postu o podanym ID. Post został prawdopodobnie już usunięty.");
        }

        Post post = postOptional.get();
        if(PermissionChecker.hasPermission(user.getRole(), User.Role.HELPER)){
            if(!post.getUser().equals(user)){
                return Response.badRequest(3, "Nie masz wymaganych uprawnień, aby usunąć ten post.");
            }
        }

        postRepository.delete(post);
        return Response.ok("Usunięto prawidłowo post.");
    }
}
