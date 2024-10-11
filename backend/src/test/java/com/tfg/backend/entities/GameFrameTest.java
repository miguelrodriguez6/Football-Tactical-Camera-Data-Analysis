package com.tfg.backend.entities;

import com.tfg.backend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
public class GameFrameTest {

    @Autowired
    private GameFrameDao gameFrameDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private PlayerDao playerDao;

    @Test
    public void testPersistAndFindGameFrame() throws Exception {
        Game game = new Game();
        game = gameDao.save(game);
        assertThat(game.getId()).isNotNull().as("Game should be saved with an ID");

        Player player = new Player();
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        WKTReader reader = new WKTReader();
        Geometry playerGeom = reader.read("POINT(10 20)");
        Geometry ballGeom = reader.read("POINT(30 40)");

        GameFrame gameFrame = new GameFrame(
                1, 1, 12.5f, true, "Player1", 15.5f, "PlayerLargeId1",
                10, 8.5f, "TeamA", playerGeom, ballGeom, 1001, player, game);
        gameFrame = gameFrameDao.save(gameFrame);
        assertThat(gameFrame.getId()).isNotNull().as("GameFrame should be saved with an ID");

        GameFrame found = gameFrameDao.findById(gameFrame.getId()).orElse(null);
        assertThat(found).isNotNull().as("GameFrame should be found by ID");

        assertThat(found.getFrameId()).isEqualTo(1);
        assertThat(found.getPeriod()).isEqualTo(1);
        assertThat(found.getGameClock()).isEqualTo(12.5f);
        assertThat(found.isLive()).isTrue();
        assertThat(found.getLastTouch()).isEqualTo("Player1");
        assertThat(found.getBallSpeed()).isEqualTo(15.5f);
        assertThat(found.getPlayerLargeId()).isEqualTo("PlayerLargeId1");
        assertThat(found.getNumber()).isEqualTo(10);
        assertThat(found.getSpeed()).isEqualTo(8.5f);
        assertThat(found.getTeamSide()).isEqualTo("TeamA");
        assertThat(found.getPlayerGeom()).isEqualTo(playerGeom);
        assertThat(found.getBallGeom()).isEqualTo(ballGeom);
        assertThat(found.getOptaId()).isEqualTo(1001);
        assertThat(found.getPlayer()).isEqualTo(player);
        assertThat(found.getGame()).isEqualTo(game);
    }
}
