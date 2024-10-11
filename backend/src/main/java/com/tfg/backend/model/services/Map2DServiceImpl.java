package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.*;
import com.tfg.backend.rest.common.BallDataFrameModel;
import com.tfg.backend.rest.common.PlayerDataFrameModel;
import com.tfg.backend.rest.common.PlayerTopThreeModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Map2DServiceImpl implements Map2DService {

    @Autowired
    StatsDao statsDao;

    @Autowired
    PlayerPositionDao playerPositionDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    PlayerGameEventsDao playerGameEventsDao;

    @Autowired
    GameDao gameDao;

    @PersistenceContext
    private EntityManager entityManager;

    private final Double imgWidth = 125 * 1.5 + 40;
    private final Double imgHeight = 86 * 1.5 + 40;
    @Override
    public List<PlayerDataFrameModel> playersDataframe(int page, int pageSize, int gameId) {
        int ini = (page - 1) * pageSize;
        List<Object[]> data = playerPositionDao.findDataFramePlayers(ini, pageSize, gameId);
        return selectPlayerDataFrameData(data, gameId);
    }

    @Override
    public List<BallDataFrameModel> ballDataframe(int page, int pageSize, int gameId) {
        int ini = (page - 1) * pageSize;
        List<Object[]> data = playerPositionDao.findDataFrameBall(ini, pageSize, gameId);
        return selectBallDataFrameData(data, gameId);
    }

    @Override
    public List<Object[]> playerInfoStats(Integer optaId, Integer gameId) {
        return getPlayerStats(optaId, gameId);
    }

    @Override
    public List<Object[]> heatData(Integer optaId, Integer gameId) {
        Optional<Game> game = gameDao.findById(Long.valueOf(gameId));
        List<Object[]> firstPeriodList = new ArrayList<>();
        if (game.isPresent()){
            double pitchLength = game.get().getPitchLength();
            double pitchWidth = game.get().getPitchWidth();
            firstPeriodList = playerPositionDao.getFirstPeriodPlayerStats(imgHeight.intValue(), imgWidth.intValue(), optaId, gameId, pitchLength, pitchWidth);
            List<Object[]> secondPeriodList = playerPositionDao.getSecondPeriodPlayerStats(imgHeight.intValue(), imgWidth.intValue(), optaId, gameId, pitchLength, pitchWidth);
            firstPeriodList.addAll(secondPeriodList);
        }

        return firstPeriodList;
    }

    @Override
    public List<PlayerTopThreeModel> topThree(String stat, Integer gameId) {
        if (stat.equals("minutes")){
            return selectPlayerTopThreeData(statsDao.getTopThreePlayersByMinutes());
        } else {
            return selectPlayerTopThreeData(getTopThreePlayersByStats(stat, gameId));
        }
    }

    @Override
    public List<PlayerGameEvents> getGoals(Integer gameId){
        Optional<Game> game = gameDao.findById(Long.valueOf(gameId));
        List<PlayerGameEvents> eventsList = new ArrayList<>();
        if (game.isPresent()){
            double pitchLength = game.get().getPitchLength();
            double pitchWidth = game.get().getPitchWidth();
            eventsList = playerGameEventsDao.findAllByTypeIdAndGameId(16, gameId);
            for (PlayerGameEvents event: eventsList){
                event.setX((float) ((event.getX()/pitchLength) + 0.5));
                event.setY((float) ((event.getY()/pitchWidth) + 0.5));
            }
        }
        return eventsList;
    }

    @Override
    public List<PlayerGameEvents> getCorners(Integer gameId){
        return playerGameEventsDao.findAllByTypeIdAndGameId(6, gameId);
    }

    @Override
    public List<PlayerGameEvents> getEvents(Integer gameId) {
        return playerGameEventsDao.findAllByGameId(gameId);
    }

    @Override
    public List<Object[]> getAllEvents(Integer gameId) {
        return getAllEventsByGameId(gameId);
    }

    @Override
    public List<PlayerGameEvents> getFouls(Integer gameId) {
        return playerGameEventsDao.findAllByTypeIdAndGameId(4, gameId);
    }

    @Override
    public List<PlayerGameEvents> getCards(Integer gameId) {
        return playerGameEventsDao.findAllByTypeIdAndGameId(17, gameId);
    }

    @Override
    public List<PlayerGameEvents> getSubOff(Integer gameId) {
        return playerGameEventsDao.findAllByTypeIdAndGameId(18, gameId);
    }

    @Override
    public List<PlayerGameEvents> getSubOn(Integer gameId) {
        return playerGameEventsDao.findAllByTypeIdAndGameId(19, gameId);
    }

    @Override
    public List<PlayerGameEvents> getOffside(Integer gameId) {
        return playerGameEventsDao.findAllByTypeIdAndGameId(2, gameId);
    }

    private List<PlayerDataFrameModel> selectPlayerDataFrameData(List<Object[]> data, int gameId){
        Optional<Game> game = gameDao.findById((long) gameId);
        List<PlayerDataFrameModel> playerDataFrameModelList = new ArrayList<>();
        if (game.isPresent()){
            for(Object[] dataObject: data){
                double x = (Double.parseDouble(dataObject[2].toString())/game.get().getPitchLength()) + 0.5;
                double y = (Double.parseDouble(dataObject[3].toString())/game.get().getPitchWidth()) + 0.5;
                PlayerDataFrameModel playerDataFrameModel = new PlayerDataFrameModel(Double.valueOf(Double.parseDouble(dataObject[0].toString())).intValue(), Double.valueOf(Double.parseDouble(dataObject[1].toString())).intValue(), x, y, Double.valueOf(Double.parseDouble(dataObject[4].toString())).intValue(), Double.valueOf(Double.parseDouble(dataObject[5].toString())).intValue());
                playerDataFrameModelList.add(playerDataFrameModel);
            }
        }
        return playerDataFrameModelList;
    }

    private List<BallDataFrameModel> selectBallDataFrameData(List<Object[]> data, int gameId){
        Optional<Game> game = gameDao.findById((long) gameId);
        List<BallDataFrameModel> ballDataFrameModelList = new ArrayList<>();
        if (game.isPresent()){
            for(Object[] dataObject: data){
                double x = (Double.parseDouble(dataObject[1].toString())/game.get().getPitchLength()) + 0.5;
                double y = (Double.parseDouble(dataObject[2].toString())/game.get().getPitchWidth()) + 0.5;
                BallDataFrameModel ballDataFrameModel = new BallDataFrameModel(Double.valueOf(Double.parseDouble(dataObject[0].toString())).intValue(), x, y);
                ballDataFrameModelList.add(ballDataFrameModel);
            }
        }
        return ballDataFrameModelList;
    }

    private List<PlayerTopThreeModel> selectPlayerTopThreeData(List<Object[]> data){
        List<PlayerTopThreeModel> playerTopThreeModelList = new ArrayList<>();
        for(Object[] dataObject: data){
            PlayerTopThreeModel playerTopThreeModel = new PlayerTopThreeModel(dataObject[0].toString(), dataObject[1].toString(), dataObject[2].toString());
            playerTopThreeModelList.add(playerTopThreeModel);
        }
        return playerTopThreeModelList;
    }

    public List<Object[]> getTopThreePlayersByStats(String stat, Integer gameId) {
        String sql = "select p.name, s." + stat + ", t.name as team_name " +
                "from player p join player_history h on p.id = h.player_id join tfg_test.public.team t on h.team_id = t.id join tfg_test.public.stats s on p.id = s.player_id and s.game_id = " + gameId + " " +
                "order by s." + stat + " desc " +
                "limit 3";

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    public List<Object[]> getPlayerStats(Integer optaId, Integer gameId) {
        int id = Math.toIntExact(playerDao.findByOptaId(optaId).getId());
        String sql = "SELECT s.minutes, s.distance, s.walking, s.jogging, s.running, s.sprinting, s.high_speed_running, s.number_of_high_intensity_runs, s.top_speed " +
                "FROM tfg_test.public.stats s  join tfg_test.public.player p on s.player_id = p.id " +
                "WHERE p.opta_id = " + optaId + " and s.game_id = " + gameId;

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    public List<Object[]> getAllEventsByGameId(Integer gameId) {
        String sql = "SELECT e.type_id, e.minute, e.second, e.aligned_frameid, e.op_player_id , p.period, e.x, e.y, t.opta_id as opContestantId, e.side " +
                "FROM player_game_events e JOIN player_position p on e.aligned_frameid = p.frame_id and e.op_player_id = p.opta_id " +
                "JOIN player p2 on e.player_id = p2.id JOIN player_history h on p2.id = h.player_id JOIN tfg_test.public.team t on t.id=h.team_id " +
                "where e.game_id = " + gameId;

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
