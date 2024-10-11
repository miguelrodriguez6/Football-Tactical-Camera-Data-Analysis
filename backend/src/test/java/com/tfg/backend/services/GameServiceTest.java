package com.tfg.backend.services;

import com.tfg.backend.model.entities.*;
import com.tfg.backend.model.services.GamesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceTest {
    @InjectMocks
    private GamesServiceImpl gamesService;

    @Mock
    private GameDao gameDao;

    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGamesByUser_AdminUser_ReturnsAllGames() {
        // Arrange
        when(userDao.findById(anyLong())).thenReturn(Optional.of(new User("username", "password", "user", "user", "user@user", User.RoleType.ADMIN)));
        when(gameDao.findAll()).thenReturn(Arrays.asList(new Game(), new Game()));

        // Act
        List<Game> result = gamesService.getGamesByUser(1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetGame_GameExists_ReturnsGame() {
        // Arrange
        Game game = new Game();
        when(gameDao.findById(anyLong())).thenReturn(Optional.of(game));

        // Act
        Game result = gamesService.getGame(1);

        // Assert
        assertEquals(game, result);
    }

    @Test
    public void testModifyGameVideoUrl_ValidData_VideoUrlModified() {
        // Arrange
        when(gameDao.findById(anyLong())).thenReturn(Optional.of(new Game()));

        // Act
        gamesService.modifyGameVideoUrl("test.mp4", 1);

        // Assert
        verify(gameDao, times(1)).save(any(Game.class));
    }
}
