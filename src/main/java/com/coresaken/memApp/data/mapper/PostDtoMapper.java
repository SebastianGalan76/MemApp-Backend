package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.hashtag.HashtagUsage;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostDtoMapper {

    @Nullable
    public static PostDto toDTO(Post post, User user, String userIp){
        if(post == null){
            return null;
        }
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setUuid(post.getUuid());
        postDto.setAuthor(post.getUser());
        postDto.setText(post.getText());
        postDto.setContent(post.getContent());
        postDto.setType(post.getType());
        postDto.setVisibility(post.getVisibility());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setCommentAmount(post.getCommentAmount());
        postDto.setFlags(post.getFlags());

        postDto.setRating(post.getPostRatingList().stream().mapToInt(PostRating::getRatingValue).sum());
        postDto.setHashtags(post.getHashtags().stream().map(HashtagUsage::getHashtag).collect(Collectors.toSet()));

        PostDto.UserDto userDto = new PostDto.UserDto();
        Optional<PostRating> postRatingOptional = post.getPostRatingList().stream().filter(postRating -> {
            if(postRating.getUser() != null && postRating.getUser().equals(user)){
                return true;
            }
            else{
                return postRating.getUserIp().equals(userIp);
            }
        }).findFirst();
        postRatingOptional.ifPresent(postRating -> userDto.setRating(postRating.getRatingValue()));

        if(user != null){
            List<Long> postListIds = new ArrayList<>();

            post.getUserPostListPosts().forEach(postListPost -> {
                if(postListPost.getPost().equals(post)){
                    postListIds.add(postListPost.getUserPostList().getId());
                }
            });

            userDto.setPostListIds(postListIds);
        }

        postDto.setUser(userDto);

        return postDto;
    }
}
