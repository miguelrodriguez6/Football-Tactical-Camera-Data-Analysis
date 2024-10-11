package com.tfg.backend.services;

import com.tfg.backend.model.entities.GameFrameDao;
import com.tfg.backend.model.services.PlayServiceImpl;
import com.tfg.backend.model.services.PlayerClip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayServiceTest {
    @InjectMocks
    private PlayServiceImpl playService;

    @Mock
    private GameFrameDao gameFrameDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContraataque_NoData_ReturnsEmptyList() {
        // Arrange
        when(gameFrameDao.consultaContraataque(anyString(), anyString(), anyInt(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<PlayerClip> result = playService.contraataque("home", 1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDesmarqueEnRuptura_NoData_ReturnsEmptyList() {
        // Arrange
        when(gameFrameDao.consultaDesmarqueEnRuptura(anyString(), anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<PlayerClip> result = playService.desmarqueEnRuptura("home", 1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDesmarqueEnApoyo_NoData_ReturnsEmptyList() {
        // Arrange
        when(gameFrameDao.consultaDesmarqueEnApoyo(anyString(), anyString(), anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<PlayerClip> result = playService.desmarqueEnApoyo("home", 1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testAtaquePorBanda_NoData_ReturnsEmptyList() {
        // Arrange
        when(gameFrameDao.consultaAtaqueBanda(anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<PlayerClip> result = playService.ataquePorBanda("home", 1);

        // Assert
        assertEquals(0, result.size());
    }
}
