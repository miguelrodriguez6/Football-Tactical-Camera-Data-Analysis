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
public class StatsTest {
    @Autowired
    private StatsDao statsDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private GameDao gameDao;

    @Test
    public void testPersistAndFindStats() {
        Player player = new Player("John Doe", 10, "Forward", 12345);
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        Game game = new Game("Friendly Match", "2-1", LocalDateTime.now(), "http://example.com/video", 105.0, 68.0, null, null, null);
        game = gameDao.save(game);
        assertThat(game.getId()).isNotNull().as("Game should be saved with an ID");

        Stats stats = new Stats("90", 10000f, 2000f, 1500f, 1000f, 500f, 100f, 20, 30f, 25f, 800f, 300f, 200f, 10, 500f, 200f, 100f, 5, 300f, 150f, 50f, 2, game, player);
        stats = statsDao.save(stats);
        assertThat(stats.getId()).isNotNull().as("Stats should be saved with an ID");

        Stats found = statsDao.findById(stats.getId()).orElse(null);
        assertThat(found).isNotNull().as("Stats should be found by ID");

        assertThat(found.getMinutes()).isEqualTo("90");
        assertThat(found.getDistance()).isEqualTo(10000f);
        assertThat(found.getWalking()).isEqualTo(2000f);
        assertThat(found.getJogging()).isEqualTo(1500f);
        assertThat(found.getRunning()).isEqualTo(1000f);
        assertThat(found.getHighSpeedRunning()).isEqualTo(500f);
        assertThat(found.getSprinting()).isEqualTo(100f);
        assertThat(found.getNumberOfHighIntensityRuns()).isEqualTo(20);
        assertThat(found.getTopSpeed()).isEqualTo(30f);
        assertThat(found.getAverageSpeed()).isEqualTo(25f);
        assertThat(found.getDistanceTip()).isEqualTo(800f);
        assertThat(found.getHsrDistanceTip()).isEqualTo(300f);
        assertThat(found.getSprintDistanceTip()).isEqualTo(200f);
        assertThat(found.getNumberOfHighIntensityRunsTip()).isEqualTo(10);
        assertThat(found.getDistanceOtip()).isEqualTo(500f);
        assertThat(found.getHsrDistanceOtip()).isEqualTo(200f);
        assertThat(found.getSprintDistanceOtip()).isEqualTo(100f);
        assertThat(found.getNumberOfHighIntensityRunsOtip()).isEqualTo(5);
        assertThat(found.getDistanceBop()).isEqualTo(300f);
        assertThat(found.getHsrDistanceBop()).isEqualTo(150f);
        assertThat(found.getSprintDistanceBop()).isEqualTo(50f);
        assertThat(found.getNumberOfHighIntensityRunsBop()).isEqualTo(2);
        assertThat(found.getGame()).isEqualTo(game);
        assertThat(found.getPlayer()).isEqualTo(player);
    }
}
