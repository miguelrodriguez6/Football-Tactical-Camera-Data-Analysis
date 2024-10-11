package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.PlayerGameEvents;
import com.tfg.backend.rest.common.BallDataFrameModel;
import com.tfg.backend.rest.common.PlayerDataFrameModel;
import com.tfg.backend.rest.common.PlayerTopThreeModel;

import java.util.List;

public interface Map2DService {

    List<PlayerDataFrameModel> playersDataframe(int page, int pageSize, int gameId);
    List<BallDataFrameModel> ballDataframe(int page, int pageSize, int gameId);
    List<Object[]> playerInfoStats(Integer optaId, Integer gameId);
    List<Object[]> heatData(Integer optaId, Integer gameId);
    List<PlayerTopThreeModel> topThree(String stat, Integer gameId);
    List<PlayerGameEvents> getGoals(Integer gameId);
    List<PlayerGameEvents> getCorners(Integer gameId);
    List<PlayerGameEvents> getEvents(Integer gameId);
    List<Object[]> getAllEvents(Integer gameId);
    List<PlayerGameEvents> getFouls(Integer gameId);
    List<PlayerGameEvents> getCards(Integer gameId);
    List<PlayerGameEvents> getSubOff(Integer gameId);
    List<PlayerGameEvents> getSubOn(Integer gameId);
    List<PlayerGameEvents> getOffside(Integer gameId);
}