package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatsDao extends JpaRepository<Stats, Long> {

    @Query(value = """
            select p.name, s.minutes, t.name as team_name
                    from player p join tfg_test.public.player_history h on p.id=h.player_id join tfg_test.public.stats s on p.opta_id = s.id join tfg_test.public.team t on h.team_id = t.id
                    order by cast(SUBSTRING(s.minutes , 1, position(':' in s.minutes) - 1) as int) desc, cast(SUBSTRING(s.minutes , position(':' in s.minutes)+1, 3) as int) desc
                    limit 3
            """, nativeQuery = true)
    List<Object[]> getTopThreePlayersByMinutes();

    @Query(value = """
            select sum(s.distance)
            from tfg_test.public.stats s join tfg_test.public.player p on s.player_id=p.id join public.player_history ph on p.id = ph.player_id join tfg_test.public.team t on ph.team_id=t.id
            where s.game_id=:gameId and t.opta_id=:teamOptaId
            """, nativeQuery = true)
    int getTotalDistanceByTeam(@Param("gameId") int gameId, @Param("teamOptaId") int teamOptaId);

    @Query(value = """
SELECT sum(s.running), sum(s.sprinting), sum(s.jogging), sum(s.walking)
FROM tfg_test.public.stats s JOIN player p ON s.player_id = p.id JOIN public.player_history ph on p.id = ph.player_id JOIN public.team t on ph.team_id = t.id
WHERE s.game_id=:gameId AND t.opta_id=:teamOptaId
""", nativeQuery = true)
    Object[] getTotalTeamStatsByGameId(@Param("gameId") int gameId, @Param("teamOptaId") int teamOptaId);

    @Query(value = """
SELECT *
FROM tfg_test.public.stats s
WHERE game_id=:gameId
ORDER BY top_speed DESC
LIMIT 3;
""", nativeQuery = true)
    List<Stats> getTopThreeTopSpeedByGame(@Param("gameId") int gameId);

    @Query(value = """
SELECT s.*
FROM stats s JOIN player p on s.player_id=p.id
WHERE p.opta_id=:optaId AND s.game_id=:gameId
""", nativeQuery = true)
    Stats getStatByOptaIdAndGameId(@Param("gameId") int gameId, @Param("optaId") int optaId);
}
