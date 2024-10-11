package com.tfg.backend.model.services;

import java.util.List;

public interface PlayService {

    List<PlayerClip> contraataque(String side, Integer gameId); //otherSide, goal y otherGoal se definen a partir de side
    List<PlayerClip> desmarqueEnRuptura(String side, Integer gameId); //goal es 60 o -60, otherSide a partir de side
    List<PlayerClip> desmarqueEnApoyo(String side,Integer gameId); //otherSide a partir de side
    List<PlayerClip> ataquePorBanda(String side, Integer gameId); //goal 60 o -60, otherSide no se usa
}
