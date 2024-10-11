package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.entities.UserDao;
import com.tfg.backend.model.exceptions.DuplicateInstanceException;
import com.tfg.backend.model.exceptions.IncorrectLoginException;
import com.tfg.backend.model.exceptions.IncorrectPasswordException;
import com.tfg.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionChecker permissionChecker;


    @Override
    public void signUp(User user) throws DuplicateInstanceException {

        if (userDao.existsByUserName(user.getUserName())) {
            throw new DuplicateInstanceException("User already exists", user.getUserName());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.RoleType.DEFAULT);

        userDao.save(user);

    }

    @Override
    @Transactional(readOnly = true)
    public User login(String userName, String password) throws IncorrectLoginException {

        Optional<User> user = userDao.findByUserName(userName);

        if (!user.isPresent()) {
            throw new IncorrectLoginException(userName, password);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new IncorrectLoginException(userName, password);
        }

        return user.get();

    }

    @Override
    public User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException {

        User user = permissionChecker.checkUser(id);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        return user;

    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword)
            throws IncorrectPasswordException, InstanceNotFoundException {

        User user = permissionChecker.checkUser(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException();
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

    }

}
