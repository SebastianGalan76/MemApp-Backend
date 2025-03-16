package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.dto.PostFlagDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostFlag;
import com.coresaken.memApp.database.repository.post.PostFlagRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import com.coresaken.memApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostFlagService {
    final UserService userService;

    final PostRepository postRepository;
    final PostFlagRepository postFlagRepository;

    public ResponseEntity<Response> setPostFlag(PostFlagDto postFlagDto) {
        Optional<Post> postOptional = postRepository.findById(postFlagDto.getPostId());
        if(postOptional.isEmpty()){
            return Response.badRequest(2, "Brak posta o podanym ID. Post został prawdopodobnie usunięty.");
        }

        Post post = postOptional.get();
        return setPostFlag(post, postFlagDto.getFlags());
    }

    public ResponseEntity<Response> setPostFlag(Post post, Set<PostFlag.FlagType> flags){
        User user = userService.getLoggedInUser();

        boolean isStaff = user != null && PermissionChecker.hasPermission(user.getRole(), User.Role.HELPER);

        List<PostFlag> currentFlags = post.getFlags();

        if(currentFlags != null){
            Iterator<PostFlag> iterator = currentFlags.iterator();
            while (iterator.hasNext()) {
                PostFlag postFlag = iterator.next();
                PostFlag.FlagType type = flags
                        .stream()
                        .filter(flag -> postFlag.getType() == flag)
                        .findFirst()
                        .orElse(null);
                if (type == null) {
                    if(hasPermissionToChange(isStaff, postFlag.isStaffFlag())){
                        iterator.remove();
                        postFlagRepository.delete(postFlag);
                    }
                }

                if(type != null){
                    if(isStaff){
                        postFlag.setStaffFlag(true);
                        postFlagRepository.save(postFlag);
                    }
                }
            }
        }

        //Adding new flags
        flags.forEach(flagType -> {
            boolean isSet = currentFlags != null && currentFlags.stream().anyMatch(postFlag -> postFlag.getType() == flagType);

            if(!isSet){
                PostFlag postFlag = new PostFlag();
                postFlag.setType(flagType);
                postFlag.setPost(post);
                postFlag.setStaffFlag(isStaff);

                postFlagRepository.save(postFlag);
            }
        });


        return Response.ok("Ustawiono prawidłowo nowe flagi posta.");
    }

    private boolean hasPermissionToChange(boolean isUserStaff, boolean isStaffFlag){
        if (isUserStaff){
            return true;
        }

        return !isStaffFlag;
    }
}
