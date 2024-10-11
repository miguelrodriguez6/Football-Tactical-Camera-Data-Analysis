package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerPositionDao extends JpaRepository<PlayerPosition, Long> {

    @Query(value = """
            SELECT ((p.y/:pitchWidth) + 0.5) * (:imgHeight -40) + 20, ((p.x/:pitchLength) + 0.5) * (:imgWidth - 40) + 20
                    FROM player_position p
                    WHERE p.opta_id = :playerId and p.period = 1 and p.game_id = :gameId
            """, nativeQuery = true)
    List<Object[]> getFirstPeriodPlayerStats(@Param("imgHeight") int imgHeight, @Param("imgWidth") int imgWidth, @Param("playerId") int playerId, @Param("gameId") int gameId, @Param("pitchLength") double pitchLength, @Param("pitchWidth") double pitchWidth);

    @Query(value = """
            SELECT :imgHeight - (((p.y/:pitchWidth) + 0.5) * (:imgHeight - 40) + 20), :imgWidth - (((p.x/:pitchLength) + 0.5) * (:imgWidth - 40) + 20)
                    FROM player_position p
                    WHERE p.opta_id = :playerId and p.period=2 and p.game_id = :gameId
            """, nativeQuery = true)
    List<Object[]> getSecondPeriodPlayerStats(@Param("imgHeight") int imgHeight, @Param("imgWidth") int imgWidth, @Param("playerId") int playerId, @Param("gameId") int gameId, @Param("pitchLength") double pitchLength, @Param("pitchWidth") double pitchWidth);

    @Query(value = """
            select p.opta_id, p.frame_id, p.x, p.y, player_history.team_id, pl.number
                    from player_position p join player pl on p.opta_id=pl.opta_id join player_history on pl.id = player_history.player_id join tfg_test.public.team t on player_history.team_id = t.id
                    where p.game_id = :gameId
                    order by p.frame_id, t.opta_id, opta_id
                    offset :ini limit :pageSize""", nativeQuery = true)
    List<Object[]> findDataFramePlayers(@Param("ini") int ini, @Param("pageSize") int pageSize, @Param("gameId") int gameId);

    @Query(value = """
            select  b.frame_id, b.x, b.y
                    from tfg_test.public.ball b
                    where b.game_id = :gameId
                    order by b.frame_id
                    offset :ini limit :pageSize""", nativeQuery = true)
    List<Object[]> findDataFrameBall(@Param("ini") int ini, @Param("pageSize") int pageSize, @Param("gameId") int gameId);
}
