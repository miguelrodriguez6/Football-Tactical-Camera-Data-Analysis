package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.entities.UserDao;
import com.tfg.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker {

    @Autowired
    private UserDao userDao;

    @Override
    public void checkUserExists(Long userId) throws InstanceNotFoundException {
        if (!userDao.existsById(userId)) {
            throw new InstanceNotFoundException("User not found", userId);
        }
    }

    @Override
    public User checkUser(Long userId) throws InstanceNotFoundException {
        Optional<User> user = userDao.findById(userId);
        if (!user.isPresent()) {
            throw new InstanceNotFoundException("User not found", userId);
        }
        return user.get();
    }
}
