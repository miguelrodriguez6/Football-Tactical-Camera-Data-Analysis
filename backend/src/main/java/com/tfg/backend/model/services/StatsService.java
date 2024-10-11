package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.Stats;
import com.tfg.backend.model.entities.StatsDao;

import java.util.List;
import java.util.Map;

public interface StatsService {

    String getStats(String side, Integer gameId);
    Object[] getPossession(Integer gameId);
    Integer getDistance(Integer gameId, Integer teamOptaId);
    Object[] getTeamStats(Integer gameId, Integer teamOptaId);
    List<Stats> getTopThreeByTopSpeedAndGameId(Integer gameId);
    Stats getPlayerStatsByGameId(Integer gameId, Integer playerId);
}
