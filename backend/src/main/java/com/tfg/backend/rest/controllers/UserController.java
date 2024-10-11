package com.tfg.backend.rest.controllers;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.exceptions.*;
import com.tfg.backend.model.services.UserService;
import com.tfg.backend.rest.dtos.LoginParamsDto;
import com.tfg.backend.rest.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tfg.backend.rest.dtos.UserConversor.toUser;
import static com.tfg.backend.rest.dtos.UserConversor.toUserDto;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public UserDto signUp(@RequestBody UserDto userDto) throws DuplicateInstanceException {
        User user = toUser(userDto);
        userService.signUp(user);
        return toUserDto(user);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginParamsDto params) throws IncorrectLoginException {
        User user = userService.login(params.getUserName(), params.getPassword());
        return toUserDto(user);
    }

    @PutMapping("/{id}")
    public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable Long id, @RequestBody UserDto userDto) throws InstanceNotFoundException, PermissionException {
        if (!id.equals(userId)) {
            throw new PermissionException();
        }

        return toUserDto(userService.updateProfile(id, userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail()));

    }
}
