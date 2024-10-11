package com.tfg.backend.model.services;

import com.tfg.backend.model.entities.Player;
import com.tfg.backend.model.entities.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerDataServiceImpl implements PlayerDataService{

    @Autowired
    PlayerDao playerDao;

    @Override
    public List<Player> getPlayers(){
        return playerDao.findAll();
    }

    @Override
    public Optional<Player> getPlayerById(Long id){
        return playerDao.findById(id);
    }

    @Override
    public Player getPlayerByOptaId(Integer optaId){
        return playerDao.findByOptaId(optaId);
    }

    @Override
    public List<Player> getHomeTeamPlayers(Integer gameId) {
        return playerDao.obtenerJugadoresEquipoLocal(gameId);
    }

    @Override
    public List<Player> getAwayTeamPlayers(Integer gameId) {
        return playerDao.obtenerJugadoresEquipoVisitante(gameId);
    }
}
