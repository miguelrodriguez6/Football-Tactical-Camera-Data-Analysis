package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.GameFrameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayServiceImpl implements PlayService {

    @Autowired
    GameFrameDao gameFrameDao;

    @Override
    public List<PlayerClip> contraataque(String side, Integer gameId) {
        String otherSide;
        int goal;
        int otherGoal;
        if (side.equals("home")){
            otherSide = "away";
            goal = 1;
            otherGoal = -1;
        } else {
            otherSide = "home";
            goal = -1;
            otherGoal = 1;
        }
        List<Object[]> data = gameFrameDao.consultaContraataque(side, otherSide, goal, otherGoal, gameId);
        return selectData(data);
    }

    @Override
    public List<PlayerClip> desmarqueEnRuptura(String side, Integer gameId) {
        String otherSide;
        int goal;
        if (side.equals("home")){
            otherSide = "away";
            goal = 60;
        } else {
            otherSide = "home";
            goal = -60;
        }
        List<Object[]> data = gameFrameDao.consultaDesmarqueEnRuptura(side, otherSide, goal, gameId);
        return selectData(data);
    }

    @Override
    public List<PlayerClip> desmarqueEnApoyo(String side, Integer gameId) {
        String otherSide;
        if (side.equals("home")){
            otherSide = "away";
        } else {
            otherSide = "home";
        }
        List<Object[]> data = gameFrameDao.consultaDesmarqueEnApoyo(side, otherSide, gameId);
        return selectData(data);
    }

    @Override
    public List<PlayerClip> ataquePorBanda(String side, Integer gameId) {
        int goal;
        if (side.equals("home")){
            goal = 60;
        } else {
            goal = -60;
        }
        List<Object[]> data = gameFrameDao.consultaAtaqueBanda(side, goal, gameId);
        return selectData(data);
    }

    private List<PlayerClip> selectData(List<Object[]> data){
        List<PlayerClip> playerClipList = new ArrayList<>();
        for(Object[] dataObject: data){
            PlayerClip playerClip = new PlayerClip( Double.valueOf(Double.parseDouble(dataObject[1].toString())).intValue(), Double.valueOf(Double.parseDouble(dataObject[2].toString())).intValue(), Double.valueOf(Double.parseDouble(dataObject[0].toString())).intValue(), dataObject[4].toString());
            playerClipList.add(playerClip);
        }
        return playerClipList;
    }
}
