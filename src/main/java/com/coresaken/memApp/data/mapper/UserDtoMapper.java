package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.database.model.User;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UserDtoMapper {
    @Nullable
    public static UserDto toDTO(User user){
        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());

        userDto.setOwnedMemeLists(user.getOwnerPostList());
        return userDto;
    }
}
