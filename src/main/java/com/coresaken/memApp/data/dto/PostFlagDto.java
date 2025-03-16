package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.post.PostFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFlagDto {
    Long postId;

    Set<PostFlag.FlagType> flags;
}
