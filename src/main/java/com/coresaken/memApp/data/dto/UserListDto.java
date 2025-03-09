package com.coresaken.memApp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {
    Long id;
    UUID uuid;

    String name;
    AuthorDto author;

    Page<PostDto> content;
}
