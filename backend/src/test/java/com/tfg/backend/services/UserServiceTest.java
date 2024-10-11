package com.tfg.backend.services;

import com.tfg.backend.model.entities.User;
import com.tfg.backend.model.entities.UserDao;
import com.tfg.backend.model.exceptions.DuplicateInstanceException;
import com.tfg.backend.model.exceptions.IncorrectLoginException;
import com.tfg.backend.model.exceptions.IncorrectPasswordException;
import com.tfg.backend.model.exceptions.InstanceNotFoundException;
import com.tfg.backend.model.services.PermissionChecker;
import com.tfg.backend.model.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private PermissionChecker permissionChecker;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_UserDoesNotExist_ShouldSaveUser() throws DuplicateInstanceException {
        // Arrange
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");

        when(userDao.existsByUserName("testUser")).thenReturn(false);

        // Act
        assertDoesNotThrow(() -> userService.signUp(user));

        // Assert
        verify(passwordEncoder).encode("password");
        verify(userDao).save(user);
    }

    @Test
    void signUp_UserAlreadyExists_ShouldThrowDuplicateInstanceException() {
        // Arrange
        User existingUser = new User();
        existingUser.setUserName("existingUser");

        when(userDao.existsByUserName("existingUser")).thenReturn(true);

        // Act and Assert
        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(existingUser));
    }

    @Test
    void login_CorrectCredentials_ShouldReturnUser() throws IncorrectLoginException {
        // Arrange
        User existingUser = new User();
        existingUser.setUserName("existingUser");
        existingUser.setPassword("encodedPassword");

        when(userDao.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        // Act
        User result = userService.login("existingUser", "password");

        // Assert
        assertEquals(existingUser, result);
    }

    @Test
    void login_IncorrectUserName_ShouldThrowIncorrectLoginException() {
        // Arrange
        when(userDao.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IncorrectLoginException.class, () -> userService.login("nonExistingUser", "password"));
    }

    @Test
    void login_IncorrectPassword_ShouldThrowIncorrectLoginException() {
        // Arrange
        User existingUser = new User();
        existingUser.setUserName("existingUser");
        existingUser.setPassword("encodedPassword");

        when(userDao.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act and Assert
        assertThrows(IncorrectLoginException.class, () -> userService.login("existingUser", "wrongPassword"));
    }

    @Test
    void updateProfile_ValidId_ShouldUpdateUser() throws InstanceNotFoundException {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");

        when(permissionChecker.checkUser(1L)).thenReturn(existingUser);

        // Act
        User result = userService.updateProfile(1L, "Jane", "Doe", "jane.doe@example.com");

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jane.doe@example.com", result.getEmail());
    }

    @Test
    void changePassword_IncorrectOldPassword_ShouldThrowIncorrectPasswordException() throws InstanceNotFoundException {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldEncodedPassword");

        when(permissionChecker.checkUser(1L)).thenReturn(existingUser);
        when(passwordEncoder.matches("wrongPassword", "oldEncodedPassword")).thenReturn(false);

        // Act and Assert
        assertThrows(IncorrectPasswordException.class, () -> userService.changePassword(1L, "wrongPassword", "newPassword"));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
