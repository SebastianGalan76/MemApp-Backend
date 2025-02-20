package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.dto.CommentDto;
import com.coresaken.memApp.data.dto.PostDto;
import com.coresaken.memApp.database.model.Comment;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.model.post.PostRating;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDtoMapper {

    @Nullable
    public static CommentDto toDTO(Comment comment, User user, String userIp){
        if(comment == null){
            return null;
        }
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setEdited(comment.isEdited());

        commentDto.setAuthor(comment.getAuthor());

        if(comment.getReplies() != null){
            commentDto.setReplies(comment.getReplies().stream().map(c -> CommentDtoMapper.toDTO(c, user, userIp)).limit(3).toList());
        }

        return commentDto;
    }
}
