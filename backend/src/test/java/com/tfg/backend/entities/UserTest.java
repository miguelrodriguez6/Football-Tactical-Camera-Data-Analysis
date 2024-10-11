package com.tfg.backend.entities;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.entities.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class UserTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testPersistAndFindUser() {
        User user = new User("john_doe", "password123", "John", "Doe", "john.doe@example.com");
        user.setRole(User.RoleType.DEFAULT);
        user = userDao.save(user);
        assertThat(user.getId()).isNotNull().as("User should be saved with an ID");

        User found = userDao.findById(user.getId()).orElse(null);
        assertThat(found).isNotNull().as("User should be found by ID");

        assertThat(found.getUserName()).isEqualTo("john_doe");
        assertThat(found.getPassword()).isEqualTo("password123");
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
        assertThat(found.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(found.getRole()).isEqualTo(User.RoleType.DEFAULT);
    }
}
