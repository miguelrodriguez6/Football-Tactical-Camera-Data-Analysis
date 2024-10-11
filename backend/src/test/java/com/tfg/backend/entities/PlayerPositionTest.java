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
public class PlayerPositionTest {
    @Autowired
    private PlayerPositionDao playerPositionDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private GameDao gameDao;

    @Test
    public void testPersistAndFindPlayerPosition() {
        Player player = new Player("John Doe", 10, "Forward", 12345);
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        Game game = new Game("Friendly Match", "2-1", LocalDateTime.now(), "http://example.com/video", 105.0, 68.0, null, null, null);
        game = gameDao.save(game);
        assertThat(game.getId()).isNotNull().as("Game should be saved with an ID");

        PlayerPosition position = new PlayerPosition(12345, 1, 1, 50.5f, 75.5f, player, game);
        position = playerPositionDao.save(position);
        assertThat(position.getId()).isNotNull().as("Position should be saved with an ID");

        PlayerPosition found = playerPositionDao.findById(position.getId()).orElse(null);
        assertThat(found).isNotNull().as("Position should be found by ID");

        assertThat(found.getOptaId()).isEqualTo(12345);
        assertThat(found.getFrameId()).isEqualTo(1);
        assertThat(found.getPeriod()).isEqualTo(1);
        assertThat(found.getX()).isEqualTo(50.5f);
        assertThat(found.getY()).isEqualTo(75.5f);
        assertThat(found.getPlayer()).isEqualTo(player);
        assertThat(found.getGame()).isEqualTo(game);
    }
}
