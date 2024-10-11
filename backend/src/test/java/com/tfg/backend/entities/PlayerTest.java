package com.tfg.backend.entities;

import com.tfg.backend.model.entities.Player;
import com.tfg.backend.model.entities.PlayerDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class PlayerTest {
    @Autowired
    private PlayerDao playerDao;

    @Test
    public void testPersistAndFindPlayer() {
        Player player = new Player("John Doe", 10, "Forward", 12345);
        player = playerDao.save(player);
        assertThat(player.getId()).isNotNull().as("Player should be saved with an ID");

        Player found = playerDao.findById(player.getId()).orElse(null);
        assertThat(found).isNotNull().as("Player should be found by ID");

        assertThat(found.getName()).isEqualTo("John Doe");
        assertThat(found.getNumber()).isEqualTo(10);
        assertThat(found.getPosition()).isEqualTo("Forward");
        assertThat(found.getOptaId()).isEqualTo(12345);
    }
}
