package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDtoMapper {

    @Nullable
    public static PostDto toDTO(Post post, User user, String userIp){
        if(post == null){
            return null;
        }
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setOwner(post.getUser());
        postDto.setText(post.getText());
        postDto.setContent(post.getContent());
        postDto.setType(post.getType());
        postDto.setVisibility(post.getVisibility());
        postDto.setCreatedAt(post.getCreatedAt());

        postDto.setRating(post.getPostRatingList().stream().mapToInt(PostRating::getRatingValue).sum());

        PostDto.UserDto userDto = new PostDto.UserDto();
        Optional<PostRating> postRatingOptional = post.getPostRatingList().stream().filter(postRating -> {
            if(postRating.getUser().equals(user)){
                return true;
            }
            else{
                return postRating.getUserIp().equals(userIp);
            }
        }).findFirst();
        postRatingOptional.ifPresent(postRating -> userDto.setRating(postRating.getRatingValue()));

        if(user != null){
            List<Long> postListIds = new ArrayList<>();

            post.getUserPostList().forEach(postList -> {
                if(postList.getSavedPosts().contains(post)){
                    postListIds.add(postList.getId());
                }
            });

            userDto.setPostListIds(postListIds);
        }

        postDto.setUser(userDto);

        return postDto;
    }
}
