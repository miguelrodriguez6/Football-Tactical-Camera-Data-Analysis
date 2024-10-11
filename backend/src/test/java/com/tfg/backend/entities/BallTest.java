package com.tfg.backend.entities;

import com.tfg.backend.model.entities.Ball;
import com.tfg.backend.model.entities.BallDao;
import com.tfg.backend.model.entities.Game;
import com.tfg.backend.model.entities.GameDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class BallTest {

    @Autowired
    private BallDao ballDao;

    @Autowired
    private GameDao gameDao;

    @Test
    public void testPersistAndFindBall() {
        Game game = new Game();
        game = gameDao.save(game);

        Ball ball = new Ball(1, 10.5f, 20.5f, game);
        ball = ballDao.save(ball);

        Ball found = ballDao.findById(ball.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getFrameId()).isEqualTo(1);
        assertThat(found.getX()).isEqualTo(10.5f);
        assertThat(found.getY()).isEqualTo(20.5f);
        assertThat(found.getGame()).isEqualTo(game);

    }
}
