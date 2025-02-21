package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.dto.CommentDto;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.comment.CommentRating;
import com.coresaken.memApp.database.model.post.PostRating;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(comment.getCommentRatingList() != null){
            Map<Byte, Long> rateCounts = comment.getCommentRatingList().stream()
                    .collect(Collectors.groupingBy(CommentRating::getRatingValue, Collectors.counting()));

            commentDto.setLikeAmount(rateCounts.getOrDefault((byte)1, 0L).intValue());
            commentDto.setDislikeAmount(rateCounts.getOrDefault((byte)-1, 0L).intValue());

            Optional<CommentRating> commentRatingOptional = comment.getCommentRatingList().stream().filter(commentRating -> {
                if(commentRating.getUser().equals(user)){
                    return true;
                }
                else{
                    return commentRating.getUserIp().equals(userIp);
                }
            }).findFirst();
            commentRatingOptional.ifPresent(commentRating -> commentDto.setUserRating(commentRating.getRatingValue()));
        }

        return commentDto;
    }
}
