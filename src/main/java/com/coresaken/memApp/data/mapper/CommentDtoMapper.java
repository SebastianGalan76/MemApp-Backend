package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.dto.CommentDto;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.User;
import jakarta.annotation.Nullable;

import java.util.ArrayList;

public class CommentDtoMapper {

    @Nullable
    public static CommentDto toDTO(Comment comment, User user, String userIp, int recursion){
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
            if(comment.getPost() != null || recursion>=2){
                commentDto.setReply(new CommentDto.ReplyDto(comment.getReplies().size(), new ArrayList<>()));
            }
            else{
                final int recursionF = ++recursion;
                commentDto.setReply(new CommentDto.ReplyDto(
                        comment.getReplies().size(),
                        comment.getReplies().stream()
                                .map(c -> CommentDtoMapper.toDTO(c, user, userIp, recursionF))
                                .limit(3)
                                .toList())
                );
            }
        }
        else{
            commentDto.setReply(new CommentDto.ReplyDto(0, new ArrayList<>()));
        }

        return commentDto;
    }
}
