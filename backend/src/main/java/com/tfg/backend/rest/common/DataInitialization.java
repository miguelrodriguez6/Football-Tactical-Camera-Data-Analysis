package com.tfg.backend.rest.common;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.entities.UserDao;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitialization {

    @Autowired
    UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void inicializar() {
        if (!userDao.existsByUserName("admin")){
            User user = new User("admin", passwordEncoder.encode("admin"), "admin", "admin", "admin@admin");
            user.setRole(User.RoleType.ADMIN);
            userDao.save(user);
        }
    }
}
