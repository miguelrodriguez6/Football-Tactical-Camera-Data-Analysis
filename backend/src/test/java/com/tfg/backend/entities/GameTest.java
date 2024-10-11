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
public class GameTest {
    @Autowired
    private GameDao gameDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TeamDao teamDao;

    @Test
    public void testPersistAndFindGame() {
        User user = new User();
        user = userDao.save(user);
        assertThat(user.getId()).isNotNull().as("User should be saved with an ID");

        Team homeTeam = new Team();
        homeTeam = teamDao.save(homeTeam);
        assertThat(homeTeam.getId()).isNotNull().as("Home team should be saved with an ID");

        Team awayTeam = new Team();
        awayTeam = teamDao.save(awayTeam);
        assertThat(awayTeam.getId()).isNotNull().as("Away team should be saved with an ID");

        Game game = new Game("Friendly Match", "2-1", LocalDateTime.now(), "http://example.com/video", 105.0, 68.0, user, homeTeam, awayTeam);
        game = gameDao.save(game);
        assertThat(game.getId()).isNotNull().as("Game should be saved with an ID");

        Game found = gameDao.findById(game.getId()).orElse(null);
        assertThat(found).isNotNull().as("Game should be found by ID");

        assertThat(found.getDescription()).isEqualTo("Friendly Match");
        assertThat(found.getResult()).isEqualTo("2-1");
        assertThat(found.getStartTime()).isEqualTo(game.getStartTime());
        assertThat(found.getVideoUrl()).isEqualTo("http://example.com/video");
        assertThat(found.getPitchLength()).isEqualTo(105.0);
        assertThat(found.getPitchWidth()).isEqualTo(68.0);
        assertThat(found.getUser()).isEqualTo(user);
        assertThat(found.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(found.getAwayTeam()).isEqualTo(awayTeam);
    }
}
