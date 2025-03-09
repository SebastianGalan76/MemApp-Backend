package com.coresaken.memApp.service.list;

import com.coresaken.memApp.data.dto.AuthorDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.dto.UserListDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.list.UserPostList;
import com.coresaken.memApp.database.model.list.UserPostListPost;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.list.UserPostListPostRepository;
import com.coresaken.memApp.database.repository.list.UserPostListRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPostListService {
    final UserService userService;

    final UserPostListRepository userPostListRepository;
    final UserPostListPostRepository userPostListPostRepository;
    final PostRepository postRepository;

    public ResponseEntity<ObjectResponse<UserPostList>> create(String name, String accessibility) {
        if(name == null || name.isBlank()){
            return ObjectResponse.badRequest(1, "Nazwa nie może być pusta!");
        }
        name = name.trim();

        UserPostList.Accessibility accessibilityType;
        try{
            accessibilityType = UserPostList.Accessibility.valueOf(accessibility);
        }catch (IllegalArgumentException e){
            accessibilityType = UserPostList.Accessibility.PRIVATE;
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return ObjectResponse.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        UserPostList userPostList = new UserPostList();
        userPostList.setName(name);
        userPostList.setAccessibility(accessibilityType);
        userPostList.setUuid(UUID.randomUUID());
        userPostList.setOwner(user);

        userPostList = userPostListRepository.save(userPostList);
        return ObjectResponse.ok("Stworzono poprawnie nową listę.", userPostList);
    }

    public ResponseEntity<Response> save(Long postID, Long listID) {
        UserPostList postList = userPostListRepository.findById(listID).orElse(null);
        if(postList == null){
            return Response.badRequest(1, "Nie znaleziono listy o podanym ID. Lista została prawdopodobnie usunięta!");
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return Response.badRequest(2, "Musisz się zalogować, aby dodać post do swojej listy!");
        }

        if(!postList.getOwner().equals(user)){
            return Response.badRequest(3, "Nie możesz dodać postu do tej listy!");
        }

        Post post = postRepository.findById(postID).orElse(null);
        if(post == null){
            return Response.badRequest(4, "Nie znaleziono postu o podanym ID. Post został prawdopodobnie usunięty!");
        }

        UserPostListPost userPostListPost = userPostListPostRepository.findByUserPostListAndPost(postList, post);
        if(userPostListPost != null){
            userPostListPostRepository.delete(userPostListPost);
        }
        else{
            userPostListPost = new UserPostListPost();
            userPostListPost.setUserPostList(postList);
            userPostListPost.setPost(post);
            userPostListPost.setAddedAt(LocalDateTime.now());

            userPostListPostRepository.save(userPostListPost);
        }

        return Response.ok("Dodano post do listy");
    }

    public ResponseEntity<ObjectResponse<UserListDto>> getList(String uuidString, int page, HttpServletRequest request) {
        UUID uuid;
        try{
            uuid = UUID.fromString(uuidString);
        }catch (IllegalArgumentException e){
            return ObjectResponse.badRequest(1, "Nie ma listy o podanym UUID.");
        }

        Optional<UserPostList> userPostListOptional = userPostListRepository.findByUuid(uuid);
        if(userPostListOptional.isEmpty()){
            return ObjectResponse.badRequest(1, "Nie ma listy o podanym UUID.");
        }

        User user = userService.getLoggedInUser();
        UserPostList userPostList = userPostListOptional.get();

        if(userPostList.getAccessibility() == UserPostList.Accessibility.PRIVATE
         && !userPostList.getOwner().equals(user)){
            return ObjectResponse.badRequest(2, "Nie masz wymaganych uprawnień, aby wyświetlić tę listę.");
        }

        User owner = userPostList.getOwner();

        UserListDto userListDto = new UserListDto();
        userListDto.setAuthor(new AuthorDto(owner.getId(), owner.getLogin(), owner.getAvatar()));
        userListDto.setId(userPostList.getId());
        userListDto.setUuid(userPostList.getUuid());
        userListDto.setName(userPostList.getName());

        Pageable pageable = PageRequest.of(page, 15);
        Page<UserPostListPost> posts = userPostListPostRepository.findByUserPostListOrderByAddedAtDesc(userPostList, pageable);

        String userIp = request.getRemoteAddr();
        List<PostDto> result = posts.getContent().stream().map(postListPost -> PostDtoMapper.toDTO(postListPost.getPost(), user, userIp)).toList();
        userListDto.setContent(new PageImpl<>(result, pageable, posts.getTotalElements()));

        return ObjectResponse.ok("", userListDto);
    }
}
