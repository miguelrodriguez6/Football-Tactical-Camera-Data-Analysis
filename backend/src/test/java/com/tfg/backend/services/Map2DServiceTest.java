package com.tfg.backend.services;

import com.tfg.backend.model.entities.*;
import com.tfg.backend.model.services.Map2DServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class Map2DServiceTest {

    @Mock
    private PlayerGameEventsDao playerGameEventsDao;

    @InjectMocks
    private Map2DServiceImpl map2DService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCorners_ShouldReturnCorners() {
        // Arrange
        List<PlayerGameEvents> expectedCorners = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(6, 123)).thenReturn(expectedCorners);

        // Act
        List<PlayerGameEvents> result = map2DService.getCorners(123);

        // Assert
        assertEquals(expectedCorners, result);
    }

    @Test
    void getEvents_ShouldReturnAllEvents() {
        // Arrange
        List<PlayerGameEvents> expectedEvents = new ArrayList<>();
        when(playerGameEventsDao.findAllByGameId(123)).thenReturn(expectedEvents);

        // Act
        List<PlayerGameEvents> result = map2DService.getEvents(123);

        // Assert
        assertEquals(expectedEvents, result);
    }

    @Test
    void getFouls_ShouldReturnFouls() {
        // Arrange
        List<PlayerGameEvents> expectedFouls = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(4, 123)).thenReturn(expectedFouls);

        // Act
        List<PlayerGameEvents> result = map2DService.getFouls(123);

        // Assert
        assertEquals(expectedFouls, result);
    }

    @Test
    void getCards_ShouldReturnCards() {
        // Arrange
        List<PlayerGameEvents> expectedCards = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(17, 123)).thenReturn(expectedCards);

        // Act
        List<PlayerGameEvents> result = map2DService.getCards(123);

        // Assert
        assertEquals(expectedCards, result);
    }

    @Test
    void getSubOff_ShouldReturnSubOffs() {
        // Arrange
        List<PlayerGameEvents> expectedSubOffs = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(18, 123)).thenReturn(expectedSubOffs);

        // Act
        List<PlayerGameEvents> result = map2DService.getSubOff(123);

        // Assert
        assertEquals(expectedSubOffs, result);
    }

    @Test
    void getSubOn_ShouldReturnSubOns() {
        // Arrange
        List<PlayerGameEvents> expectedSubOns = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(19, 123)).thenReturn(expectedSubOns);

        // Act
        List<PlayerGameEvents> result = map2DService.getSubOn(123);

        // Assert
        assertEquals(expectedSubOns, result);
    }

    @Test
    void getOffside_ShouldReturnOffsides() {
        // Arrange
        List<PlayerGameEvents> expectedOffsides = new ArrayList<>();
        when(playerGameEventsDao.findAllByTypeIdAndGameId(2, 123)).thenReturn(expectedOffsides);

        // Act
        List<PlayerGameEvents> result = map2DService.getOffside(123);

        // Assert
        assertEquals(expectedOffsides, result);
    }
}
