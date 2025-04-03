package com.coresaken.memApp.data.dto;

import com.coresaken.memApp.database.model.collection.UserCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCollectionDto {
    Long id;
    UUID uuid;

    String name;
    AuthorDto author;
    UserCollection.Accessibility accessibility;

    Page<PostDto> content;
}
