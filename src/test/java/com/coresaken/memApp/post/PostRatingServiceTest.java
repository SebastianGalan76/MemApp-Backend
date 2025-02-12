package com.coresaken.memApp.post;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import com.coresaken.memApp.database.repository.post.PostRatingRepository;
import com.coresaken.memApp.service.UserService;
import com.coresaken.memApp.service.post.NewPostService;
import com.coresaken.memApp.service.post.PostRatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class PostRatingServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private NewPostService newPostService;
    @Mock
    private PostRatingRepository repository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PostRatingService postRatingService;

    public PostRatingServiceTest(){
        openMocks(this);
    }

    @Test
    public void rate_incorrectRating(){
        byte rating = 2;

        ResponseEntity<Response> responseEntity = postRatingService.rate(1L, rating, request);
        assertEquals(400, responseEntity.getStatusCode().value());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(1, response.getErrorCode() );
    }

    @Test
    public void rate_incorrectPostId(){
        Long postId = 1L;
        when(newPostService.findById(postId)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = postRatingService.rate(postId, (byte) 1, request);
        assertEquals(400, responseEntity.getStatusCode().value());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(2, response.getErrorCode());
    }

    @Test
    public void rate_removeRatingWhenValueIsZero(){
        Post post = new Post();
        User user = new User();

        when(newPostService.findById(1L)).thenReturn(Optional.of(post));
        when(userService.getLoggedInUser()).thenReturn(user);

        PostRating postRating = new PostRating();
        when(repository.findByPostAndUser(post, user)).thenReturn(List.of(postRating));

        ResponseEntity<Response> responseEntity = postRatingService.rate(1L, (byte) 0, request);
        assertEquals(200, responseEntity.getStatusCode().value());

        verify(repository).delete(postRating);
    }

    @Test
    public void rate_changingRating(){
        Post post = new Post();
        User user = new User();

        when(newPostService.findById(1L)).thenReturn(Optional.of(post));
        when(userService.getLoggedInUser()).thenReturn(user);

        PostRating postRating = new PostRating();
        postRating.setRatingValue((byte) -1);
        when(repository.findByPostAndUser(post, user)).thenReturn(List.of(postRating));

        ResponseEntity<Response> responseEntity = postRatingService.rate(1L, (byte) 1, request);
        assertEquals(200, responseEntity.getStatusCode().value());

        assertEquals(1, postRating.getRatingValue());
        verify(repository).save(postRating);
    }
}
