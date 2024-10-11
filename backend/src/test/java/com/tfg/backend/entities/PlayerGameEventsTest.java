package com.tfg.backend.entities;

import com.tfg.backend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class PlayerGameEventsTest {
    @Autowired
    private PlayerGameEventsDao playerGameEventsDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private GameDao gameDao;

    @Test
    public void testPersistAndFindPlayerGameEvent() {
        Player player = new Player("John Doe", 10, "Forward", 12345);
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        Game game = new Game("Friendly Match", "2-1", LocalDateTime.now(), "http://example.com/video", 105.0, 68.0, null, null, null);
        game = gameDao.save(game);
        assertThat(game.getId()).isNotNull().as("Game should be saved with an ID");

        PlayerGameEvents event = new PlayerGameEvents(12345, 1, 12, 34, 50.5f, 75.5f, 1, 1, "left", player, game);
        event = playerGameEventsDao.save(event);
        assertThat(event.getId()).isNotNull().as("Event should be saved with an ID");

        PlayerGameEvents found = playerGameEventsDao.findById(event.getId()).orElse(null);
        assertThat(found).isNotNull().as("Event should be found by ID");

        assertThat(found.getOpPlayerId()).isEqualTo(12345);
        assertThat(found.getTypeId()).isEqualTo(1);
        assertThat(found.getMinute()).isEqualTo(12);
        assertThat(found.getSecond()).isEqualTo(34);
        assertThat(found.getX()).isEqualTo(50.5f);
        assertThat(found.getY()).isEqualTo(75.5f);
        assertThat(found.getAlignedFrameid()).isEqualTo(1);
        assertThat(found.getOutcome()).isEqualTo(1);
        assertThat(found.getSide()).isEqualTo("left");
        assertThat(found.getPlayer()).isEqualTo(player);
        assertThat(found.getGame()).isEqualTo(game);
    }
}
