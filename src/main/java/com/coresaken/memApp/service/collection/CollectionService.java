package com.coresaken.memApp.service.collection;

import com.coresaken.memApp.data.dto.AuthorDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.dto.UserCollectionDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.collection.UserCollection;
import com.coresaken.memApp.database.model.collection.UserCollectionPost;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.collection.UserCollectionPostRepository;
import com.coresaken.memApp.database.repository.collection.CollectionRepository;
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
public class CollectionService {
    final UserService userService;

    final CollectionRepository collectionRepository;
    final UserCollectionPostRepository userCollectionPostRepository;
    final PostRepository postRepository;

    public ResponseEntity<ObjectResponse<UserCollection>> create(String name, String accessibility) {
        if(name == null || name.isBlank()){
            return ObjectResponse.badRequest(1, "Nazwa nie może być pusta!");
        }
        name = name.trim();

        UserCollection.Accessibility accessibilityType;
        try{
            accessibilityType = UserCollection.Accessibility.valueOf(accessibility);
        }catch (IllegalArgumentException e){
            accessibilityType = UserCollection.Accessibility.PRIVATE;
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return ObjectResponse.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        UserCollection userCollection = new UserCollection();
        userCollection.setName(name);
        userCollection.setAccessibility(accessibilityType);
        userCollection.setUuid(UUID.randomUUID());
        userCollection.setOwner(user);

        userCollection = collectionRepository.save(userCollection);
        return ObjectResponse.ok("Stworzono poprawnie nową kolekcję.", userCollection);
    }

    public ResponseEntity<Response> save(Long postID, Long listID) {
        UserCollection postList = collectionRepository.findById(listID).orElse(null);
        if(postList == null){
            return Response.badRequest(1, "Nie znaleziono kolekcji o podanym ID. Kolekcja została prawdopodobnie usunięta!");
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return Response.badRequest(2, "Musisz się zalogować, aby dodać post do swojej kolekcji!");
        }

        if(!postList.getOwner().equals(user)){
            return Response.badRequest(3, "Nie możesz dodać postu do tej kolekcji!");
        }

        Post post = postRepository.findById(postID).orElse(null);
        if(post == null){
            return Response.badRequest(4, "Nie znaleziono posta o podanym ID. Post został prawdopodobnie usunięty!");
        }

        UserCollectionPost userCollectionPost = userCollectionPostRepository.findByUserCollectionAndPost(postList, post);
        if(userCollectionPost != null){
            userCollectionPostRepository.delete(userCollectionPost);
        }
        else{
            userCollectionPost = new UserCollectionPost();
            userCollectionPost.setUserCollection(postList);
            userCollectionPost.setPost(post);
            userCollectionPost.setAddedAt(LocalDateTime.now());

            userCollectionPostRepository.save(userCollectionPost);
        }

        return Response.ok("Dodano post do kolekcji.");
    }

    public ResponseEntity<ObjectResponse<UserCollectionDto>> getCollection(String uuidString, int page, HttpServletRequest request) {
        UUID uuid;
        try{
            uuid = UUID.fromString(uuidString);
        }catch (IllegalArgumentException e){
            return ObjectResponse.badRequest(1, "Nie ma kolekcji o podanym UUID.");
        }

        Optional<UserCollection> userPostListOptional = collectionRepository.findByUuid(uuid);
        if(userPostListOptional.isEmpty()){
            return ObjectResponse.badRequest(1, "Nie ma kolekcji o podanym UUID.");
        }

        User user = userService.getLoggedInUser();
        UserCollection userCollection = userPostListOptional.get();

        if(userCollection.getAccessibility() == UserCollection.Accessibility.PRIVATE
         && !userCollection.getOwner().equals(user)){
            return ObjectResponse.badRequest(2, "Nie masz wymaganych uprawnień, aby wyświetlić tę kolekcję.");
        }

        User owner = userCollection.getOwner();

        UserCollectionDto userCollectionDto = new UserCollectionDto();
        userCollectionDto.setAuthor(new AuthorDto(owner.getId(), owner.getLogin(), owner.getAvatar()));
        userCollectionDto.setId(userCollection.getId());
        userCollectionDto.setAccessibility(userCollection.getAccessibility());
        userCollectionDto.setUuid(userCollection.getUuid());
        userCollectionDto.setName(userCollection.getName());

        Pageable pageable = PageRequest.of(page, 15);
        Page<UserCollectionPost> posts = userCollectionPostRepository.findByUserCollectionOrderByAddedAtDesc(userCollection, pageable);

        String userIp = request.getRemoteAddr();
        List<PostDto> result = posts.getContent().stream().map(postListPost -> PostDtoMapper.toDTO(postListPost.getPost(), user, userIp)).toList();
        userCollectionDto.setContent(new PageImpl<>(result, pageable, posts.getTotalElements()));

        return ObjectResponse.ok("", userCollectionDto);
    }
}
