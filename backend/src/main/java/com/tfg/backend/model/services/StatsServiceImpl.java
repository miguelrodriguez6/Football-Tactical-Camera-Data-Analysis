package com.tfg.backend.model.services;

import com.google.gson.Gson;
import com.tfg.backend.model.entities.GameDao;
import com.tfg.backend.model.entities.GameFrameDao;
import com.tfg.backend.model.entities.Stats;
import com.tfg.backend.model.entities.StatsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    PlayService playService;

    @Autowired
    GameFrameDao gameFrameDao;

    @Autowired
    GameDao gameDao;

    @Autowired
    StatsDao statsDao;

    public Map<Integer, Integer> processStats(List<PlayerClip> playerClipList) {
        Map<Integer, Integer> stats = new HashMap<>();

        for (PlayerClip clip : playerClipList) {
            int playerId = clip.getPlayer();
            if (!stats.containsKey(playerId)) {
                stats.put(playerId, 0);
            }
            stats.put(playerId, stats.getOrDefault(playerId, 0) + 1);
        }
        return stats;
    }

    public Map<String, Integer> calculateAverageTeam(Map<String, Map<Integer, Integer>> stats) {
        Map<String, Integer> teamStats = new HashMap<>();
        for (String category : stats.keySet()){
            int total = 0;
            int counter = 0;
            for (Integer estadisticasJugador : stats.get(category).values()){
                total += estadisticasJugador;
                counter += 1;
            }
            if (counter > 0) {
                teamStats.put(category, Math.round((float) total / counter));
            } else {
                teamStats.put(category, 0);
            }
        }
        return teamStats;
    }

    @Override
    public String getStats(String side, Integer gameId) {

        List<PlayerClip> desmarqueRupturaData = playService.desmarqueEnRuptura(side, gameId);
        List<PlayerClip> ataqueBandaData = playService.ataquePorBanda(side, gameId);
        List<PlayerClip> desmarqueApoyoData = playService.desmarqueEnApoyo(side, gameId);
        List<PlayerClip> contraataqueData = playService.contraataque(side, gameId);

        Map<Integer, Integer> desmarqueRupturaStats = processStats(desmarqueRupturaData);
        Map<Integer, Integer> ataqueBandaStats = processStats(ataqueBandaData);
        Map<Integer, Integer> desmarqueApoyoStats = processStats(desmarqueApoyoData);
        Map<Integer, Integer> contraataqueStats = processStats(contraataqueData);

        Map<String, Map<Integer, Integer>> stats = new HashMap<>();
        stats.put("desmarque_ruptura", desmarqueRupturaStats);
        stats.put("ataque_banda", ataqueBandaStats);
        stats.put("desmarque_apoyo", desmarqueApoyoStats);
        stats.put("contraataque", contraataqueStats);

        Map<String, Integer> estadisticasEquipo = calculateAverageTeam(stats);
        Map<String, Object> teamAndPlayersStats = new HashMap<>();

        teamAndPlayersStats.put("team", estadisticasEquipo);
        teamAndPlayersStats.put("desmarque_ruptura", desmarqueRupturaStats);
        teamAndPlayersStats.put("ataque_banda", ataqueBandaStats);
        teamAndPlayersStats.put("desmarque_apoyo", desmarqueApoyoStats);
        teamAndPlayersStats.put("contraataque", contraataqueStats);

        Gson gson = new Gson();
        return gson.toJson(teamAndPlayersStats);
    }

    @Override
    public Object[] getPossession(Integer gameId) {
        int totalFrames = gameFrameDao.countDistinctFrameIdsByGameId(gameId);
        if (totalFrames==0) return new Object[]{0, 0};
        int totalFramesLastTouchHome = gameFrameDao.countDistinctFrameIdsByGameIdAndHomeLastTouch(gameId);
        int totalFramesLastTouchAway = totalFrames - totalFramesLastTouchHome;

        int homePossession = (int) Math.round(((double) totalFramesLastTouchHome /totalFrames) * 100);
        int awayPossession = (int) Math.round(((double) totalFramesLastTouchAway /totalFrames) * 100);

        return new Object[]{homePossession, awayPossession};
    }

    @Override
    public Integer getDistance(Integer gameId, Integer teamOptaId) {
        return statsDao.getTotalDistanceByTeam(gameId, teamOptaId);
    }

    @Override
    public Object[] getTeamStats(Integer gameId, Integer teamOptaId) {
        return statsDao.getTotalTeamStatsByGameId(gameId, teamOptaId);
    }

    @Override
    public List<Stats> getTopThreeByTopSpeedAndGameId(Integer gameId){
        return statsDao.getTopThreeTopSpeedByGame(gameId);
    }

    @Override
    public Stats getPlayerStatsByGameId(Integer gameId, Integer playerId) {
        return statsDao.getStatByOptaIdAndGameId(gameId, playerId);
    }
}
