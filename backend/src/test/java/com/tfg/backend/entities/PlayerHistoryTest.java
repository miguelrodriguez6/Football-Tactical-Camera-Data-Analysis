package com.tfg.backend.entities;

import com.tfg.backend.model.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class PlayerHistoryTest {
    @Autowired
    private PlayerHistoryDao playerHistoryDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TeamDao teamDao;

    @Test
    public void testPersistAndFindPlayerHistory() {
        Player player = new Player("John Doe", 10, "Forward", 12345);
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        Team team = new Team(99, "ARSENAL");
        team = teamDao.save(team);
        assertThat(team.getId()).isNotNull().as("Team should be saved with an ID");

        PlayerHistory history = new PlayerHistory(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 12, 31), player, team);
        history = playerHistoryDao.save(history);
        assertThat(history.getId()).isNotNull().as("History should be saved with an ID");

        PlayerHistory found = playerHistoryDao.findById(history.getId()).orElse(null);
        assertThat(found).isNotNull().as("History should be found by ID");

        assertThat(found.getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(found.getEndDate()).isEqualTo(LocalDate.of(2021, 12, 31));
        assertThat(found.getPlayer()).isEqualTo(player);
        assertThat(found.getTeam()).isEqualTo(team);
    }
}
