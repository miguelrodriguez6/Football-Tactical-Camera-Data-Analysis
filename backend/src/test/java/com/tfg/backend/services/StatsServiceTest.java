package com.tfg.backend.services;

import com.tfg.backend.model.entities.*;
import com.tfg.backend.model.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StatsServiceTest {

    @InjectMocks
    private StatsServiceImpl statsService;

    @Mock
    private StatsDao statsDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDistance() {
        // Arrange
        int gameId = 123;
        int teamOptaId = 456;
        Integer expectedDistance = 1000;

        // Mock the behavior of the gameDao
        when(statsDao.getTotalDistanceByTeam(gameId, teamOptaId)).thenReturn(expectedDistance);

        // Act
        Integer actualDistance = statsService.getDistance(gameId, teamOptaId);

        // Assert
        assertEquals(expectedDistance, actualDistance);
        verify(statsDao).getTotalDistanceByTeam(gameId, teamOptaId);
    }

    @Test
    public void testGetTeamStats() {
        // Arrange
        int gameId = 123;
        int teamOptaId = 456;
        Object[] expectedStats = {"stat1", "stat2", "stat3"};

        // Mock the behavior of the StatsDao
        when(statsDao.getTotalTeamStatsByGameId(gameId, teamOptaId)).thenReturn(expectedStats);

        // Act
        Object[] actualStats = statsService.getTeamStats(gameId, teamOptaId);

        // Assert
        assertEquals(expectedStats, actualStats);
        verify(statsDao).getTotalTeamStatsByGameId(gameId, teamOptaId);
    }

    @Test
    public void testGetTopThreeByTopSpeedAndGameId() {
        // Arrange
        int gameId = 123;
        List<Stats> expectedStats = Arrays.asList(new Stats(), new Stats(), new Stats()); // For example

        // Mock the behavior of the StatsDao
        when(statsDao.getTopThreeTopSpeedByGame(gameId)).thenReturn(expectedStats);

        // Act
        List<Stats> actualStats = statsService.getTopThreeByTopSpeedAndGameId(gameId);

        // Assert
        assertEquals(expectedStats, actualStats);
        verify(statsDao).getTopThreeTopSpeedByGame(gameId);
    }

    @Test
    public void testGetPlayerStatsByGameId() {
        // Arrange
        int gameId = 123;
        int playerId = 456;
        Stats expectedStats = new Stats();

        // Mock the behavior of the StatsDao
        when(statsDao.getStatByOptaIdAndGameId(gameId, playerId)).thenReturn(expectedStats);

        // Act
        Stats actualStats = statsService.getPlayerStatsByGameId(gameId, playerId);

        // Assert
        assertEquals(expectedStats, actualStats);
        verify(statsDao).getStatByOptaIdAndGameId(gameId, playerId);
    }

    @Test
    void getTopThreeByTopSpeedAndGameId_ShouldReturnTopThreePlayersByTopSpeed() {
        // Arrange
        List<Stats> expectedTopThree = Collections.emptyList();
        when(statsDao.getTopThreeTopSpeedByGame(123)).thenReturn(expectedTopThree);

        // Act
        List<Stats> result = statsService.getTopThreeByTopSpeedAndGameId(123);

        // Assert
        assertEquals(expectedTopThree, result);
    }

}
