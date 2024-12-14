package org.ecom.microservice.userservice.mapper;

import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.model.User;

public class UserMapper {

    public static User dtoToUserMapper(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(user.getPassword());
        user.setRoles(userDto.getRoles());
        return user;
    }

    public static UserDto userToDtoMapper(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
