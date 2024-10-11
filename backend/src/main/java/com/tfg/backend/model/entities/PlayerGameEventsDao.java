package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerGameEventsDao extends JpaRepository<PlayerGameEvents, Long> {

    @Query(value = "select * from player_game_events g where g.game_id=:gameId", nativeQuery = true)
    List<PlayerGameEvents> findAllByGameId(@Param("gameId") int gameId);

    @Query(value = "select * from player_game_events g where g.game_id=:gameId and g.type_id=:typeId", nativeQuery = true)
    List<PlayerGameEvents> findAllByTypeIdAndGameId(@Param("typeId") int typeId, @Param("gameId")int gameId);
}
