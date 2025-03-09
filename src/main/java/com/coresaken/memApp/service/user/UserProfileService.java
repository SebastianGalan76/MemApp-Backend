package com.coresaken.memApp.service.user;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.data.dto.UserProfileDto;
import com.coresaken.memApp.data.mapper.PostDtoMapper;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.UserPostList;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    final UserService userService;

    final UserRepository userRepository;
    final PostRepository postRepository;

    public ResponseEntity<ObjectResponse<UserProfileDto>> getProfile(String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if(userOptional.isEmpty()){
            return ObjectResponse.badRequest(1, "Brak profilu");
        }

        User user = userOptional.get();

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setLogin(user.getLogin());
        userProfileDto.setAvatar(user.getAvatar());
        userProfileDto.setRole(user.getRole());

        userProfileDto.setUserList(user
                .getOwnerPostList()
                .stream()
                .filter(list -> list.getAccessibility() == UserPostList.Accessibility.PUBLIC)
                .map(list -> new UserProfileDto.PostListDto(list.getId(), list.getName(), list.getSavedPosts().size()))
                .toList()
        );

        return ObjectResponse.ok("Znaleziono profil", userProfileDto);
    }

    public Page<PostDto> getUserPosts(String login, int page, HttpServletRequest request) {
        Optional<User> authorOptional = userRepository.findByLogin(login);
        if(authorOptional.isEmpty()){
            return null;
        }

        User author = authorOptional.get();
        String userIp = request.getRemoteAddr();

        Pageable pageable = PageRequest.of(page, 15);
        Page<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(author, pageable);

        User user = userService.getLoggedInUser();
        List<PostDto> result = posts.getContent().stream().map(post -> PostDtoMapper.toDTO(post, user, userIp)).toList();

        return new PageImpl<>(result, pageable, posts.getTotalElements());
    }
}
