package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDataService {
    List<Player> getPlayers();
    Optional<Player> getPlayerById(Long id);
    Player getPlayerByOptaId(Integer optaId);
    List<Player> getHomeTeamPlayers(Integer gameId);
    List<Player> getAwayTeamPlayers(Integer gameId);
}
