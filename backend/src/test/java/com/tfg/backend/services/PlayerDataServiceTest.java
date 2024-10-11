package com.tfg.backend.services;

import com.tfg.backend.model.entities.Player;
import com.tfg.backend.model.entities.PlayerDao;
import com.tfg.backend.model.services.PlayerDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayerDataServiceTest {
    @InjectMocks
    private PlayerDataServiceImpl playerDataService;

    @Mock
    private PlayerDao playerDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPlayers_NoData_ReturnsEmptyList() {
        // Arrange
        when(playerDao.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Player> result = playerDataService.getPlayers();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPlayerById_NotFound_ReturnsEmptyOptional() {
        // Arrange
        when(playerDao.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Player> result = playerDataService.getPlayerById(1L);

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetPlayerByOptaId_NotFound_ReturnsNull() {
        // Arrange
        when(playerDao.findByOptaId(anyInt())).thenReturn(null);

        // Act
        Player result = playerDataService.getPlayerByOptaId(1);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void testGetHomeTeamPlayers_NoData_ReturnsEmptyList() {
        // Arrange
        when(playerDao.obtenerJugadoresEquipoLocal(anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<Player> result = playerDataService.getHomeTeamPlayers(1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAwayTeamPlayers_NoData_ReturnsEmptyList() {
        // Arrange
        when(playerDao.obtenerJugadoresEquipoVisitante(anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<Player> result = playerDataService.getAwayTeamPlayers(1);

        // Assert
        assertEquals(0, result.size());
    }
}
