package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.exceptions.DuplicateInstanceException;
import com.tfg.backend.model.exceptions.IncorrectLoginException;
import com.tfg.backend.model.exceptions.IncorrectPasswordException;
import com.tfg.backend.model.exceptions.InstanceNotFoundException;

public interface UserService {

    void signUp(User user) throws DuplicateInstanceException;

    User login(String userName, String password) throws IncorrectLoginException;

    User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException;

    void changePassword(Long id, String oldPassword, String newPassword)
            throws InstanceNotFoundException, IncorrectPasswordException;

}