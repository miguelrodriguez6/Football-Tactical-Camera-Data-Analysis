package com.tfg.backend.entities;

import com.tfg.backend.model.entities.Team;
import com.tfg.backend.model.entities.TeamDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class TeamTest {
    @Autowired
    private TeamDao teamDao;

    @Test
    public void testPersistAndFindTeam() {
        Team team = new Team(1001, "Team A");
        team = teamDao.save(team);
        assertThat(team.getId()).isNotNull().as("Team should be saved with an ID");

        Team found = teamDao.findById(team.getId()).orElse(null);
        assertThat(found).isNotNull().as("Team should be found by ID");

        assertThat(found.getOptaId()).isEqualTo(1001);
        assertThat(found.getName()).isEqualTo("Team A");
    }
}
