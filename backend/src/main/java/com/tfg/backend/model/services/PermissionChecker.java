package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.exceptions.InstanceNotFoundException;

public interface PermissionChecker {

    void checkUserExists(Long userId) throws InstanceNotFoundException;
    User checkUser(Long userId) throws InstanceNotFoundException;
}
