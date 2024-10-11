package com.tfg.backend.rest.dtos;

import com.tfg.backend.model.entities.User;

public class UserConversor {

    private UserConversor() {
    }

    public final static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public final static User toUser(UserDto userDto) {

        return new User(userDto.getUserName(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail());
    }

}
