package com.coresaken.memApp.data.mapper;

import com.coresaken.memApp.data.UserDto;
import com.coresaken.memApp.database.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDtoMapper {
    public static UserDto toDTO(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        return userDto;
    }
}
