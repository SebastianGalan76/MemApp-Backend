package com.coresaken.memApp.service.comment;

import com.coresaken.memApp.data.dto.CommentDto;
import com.coresaken.memApp.data.dto.NewCommentDto;
import com.coresaken.memApp.data.mapper.CommentDtoMapper;
import com.coresaken.memApp.data.response.ObjectResponse;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.comment.CommentRepository;
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

@Service
@RequiredArgsConstructor
public class CommentService {
    final UserService userService;

    final CommentRepository commentRepository;
    final PostRepository postRepository;

    public Page<CommentDto> getCommentsForPost(Long id, int page, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, 15);

        Page<Comment> comments = commentRepository.findByPostId(id, pageable);

        User user = userService.getLoggedInUser();
        List<CommentDto> result = comments.getContent().stream().map(comment -> CommentDtoMapper.toDTO(comment, user, request.getRemoteAddr(), 0)).toList();

        return new PageImpl<CommentDto>(result, pageable, 15);
    }

    public Page<CommentDto> getCommentsForComment(Long id, int page, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, 15);

        Page<Comment> comments = commentRepository.findByParentCommentId(id, pageable);

        User user = userService.getLoggedInUser();
        List<CommentDto> result = comments.getContent().stream().map(comment -> CommentDtoMapper.toDTO(comment, user, request.getRemoteAddr(), 0)).toList();

        return new PageImpl<CommentDto>(result, pageable, 15);
    }

    public ResponseEntity<ObjectResponse<CommentDto>> comment(NewCommentDto newCommentDto) {
        String content = newCommentDto.getContent().trim();
        if(content.isBlank()){
            return ObjectResponse.badRequest(1, "Komentarz nie może być pusty!");
        }

        User user = userService.getLoggedInUser();
        if(user == null){
            return ObjectResponse.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        Comment comment = new Comment();
        comment.setContent(newCommentDto.getContent());
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        if(newCommentDto.getType() == NewCommentDto.Type.POST){
            Optional<Post> postOptional = postRepository.findById(newCommentDto.getId());
            if(postOptional.isEmpty()){
                return ObjectResponse.badRequest(3, "Wystąpił nieoczekiwany błąd. Post został prawdopodobnie usunięty!");
            }

            Post post = postOptional.get();
            post.setCommentAmount(post.getCommentAmount() + 1);
            comment.setPost(post);
        }
        else{
            Optional<Comment> parentCommentOptional = commentRepository.findById(newCommentDto.getId());
            if(parentCommentOptional.isEmpty()){
                return ObjectResponse.badRequest(4, "Wystąpił nieoczekiwany błąd. Komentarz został prawdopodobnie usunięty!");
            }

            comment.setParentComment(parentCommentOptional.get());
        }

        comment = commentRepository.save(comment);
        return ObjectResponse.ok("Komentarz został prawidłowo dodany", CommentDtoMapper.toDTO(comment, user, null, 0));
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }

    public ResponseEntity<Response> delete(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);

        if(commentOptional.isEmpty()){
            return Response.badRequest(1, "Wystąpił nieoczekiwany błąd. Komentarz został już prawdopodobnie usunięty!");
        }

        User user = userService.getLoggedInUser();

        if(user == null){
            return Response.badRequest(2, "Twoja sesja wygasła. Zaloguj się ponownie!");
        }

        Comment comment = commentOptional.get();
        if(!comment.getAuthor().equals(user)){
            return Response.badRequest(3, "Nie masz uprawnień, aby usunąć ten komentarz!");
        }

        Post post = comment.getPost();
        if(post != null){
            post.setCommentAmount(post.getCommentAmount() - 1);
            postRepository.save(post);
        }

        commentRepository.delete(comment);
        return Response.ok("Usunięto prawidłowo komentarz");
    }
}
