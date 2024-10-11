package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerDao extends JpaRepository<Player, Long> {
    Player findByOptaId(int optaId);

    @Query(value = """
            SELECT DISTINCT p.*
            FROM player p
            INNER JOIN game_frame h ON p.opta_id = h.opta_id
            WHERE h.team_side = 'home' and h.game_id = :gameId
            ORDER BY p.number;
            """, nativeQuery = true)
    List<Player> obtenerJugadoresEquipoLocal(@Param("gameId") int gameId);
    @Query(value = """
            SELECT DISTINCT p.*
            FROM player p
            INNER JOIN game_frame h ON p.opta_id = h.opta_id
            WHERE h.team_side = 'away' and h.game_id = :gameId
            ORDER BY p.number;
            """, nativeQuery = true)
    List<Player> obtenerJugadoresEquipoVisitante(@Param("gameId") int gameId);
}
