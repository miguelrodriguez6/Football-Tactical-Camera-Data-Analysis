package com.tfg.backend.rest.controllers;

import com.tfg.backend.model.entities.PlayerGameEvents;
import com.tfg.backend.model.services.Map2DService;
import com.tfg.backend.rest.common.BallDataFrameModel;
import com.tfg.backend.rest.common.PlayerDataFrameModel;
import com.tfg.backend.rest.common.PlayerTopThreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/2d")
public class Map2DController {

    @Autowired
    Map2DService map2DService;

    @GetMapping("/dataframe/players")
    public List<PlayerDataFrameModel> dataframePlayers(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "page_size", defaultValue = "100") Integer page_size, @RequestParam(value = "game_id") Integer gameId){
        return map2DService.playersDataframe(page, page_size, gameId);
    }

    @GetMapping("/dataframe/ball")
    public List<BallDataFrameModel> dataframeBall(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "page_size", defaultValue = "100") Integer page_size, @RequestParam(value = "game_id") Integer gameId){
        return map2DService.ballDataframe(page, page_size, gameId);
    }

    @GetMapping("/player_stats/{playerId}/{gameId}")
    public List<Object[]> playerStats(@PathVariable Integer playerId, @PathVariable Integer gameId){
        return map2DService.playerInfoStats(playerId, gameId);
    }

    @GetMapping("/heat_data/{playerId}/{gameId}")
    public List<Object[]> heatData(@PathVariable Integer playerId, @PathVariable Integer gameId){
        return map2DService.heatData(playerId, gameId);
    }

    @GetMapping("/top_three/{stat}/{gameId}")
    public List<PlayerTopThreeModel> getTopThreeByStat(@PathVariable String stat, @PathVariable Integer gameId){
        return map2DService.topThree(stat, gameId);
    }

    @GetMapping("/getGoals/{gameId}")
    public List<PlayerGameEvents> getGoals(@PathVariable Integer gameId){
        return map2DService.getGoals(gameId);
    }

    @GetMapping("/getCorners/{gameId}")
    public List<PlayerGameEvents> getCorners(@PathVariable Integer gameId){
        return map2DService.getCorners(gameId);
    }

    @GetMapping("/getCards/{gameId}")
    public List<PlayerGameEvents> getCards(@PathVariable Integer gameId){
        return map2DService.getCards(gameId);
    }

    @GetMapping("/getFoults/{gameId}")
    public List<PlayerGameEvents> getFouls(@PathVariable Integer gameId){
        return map2DService.getFouls(gameId);
    }

    @GetMapping("/getSubOn/{gameId}")
    public List<PlayerGameEvents> getSubOn(@PathVariable Integer gameId){
        return map2DService.getSubOn(gameId);
    }

    @GetMapping("/getSubOff/{gameId}")
    public List<PlayerGameEvents> getSubOff(@PathVariable Integer gameId){
        return map2DService.getSubOff(gameId);
    }

    @GetMapping("/getEvents/{gameId}")
    public List<Object[]> getEvents(@PathVariable Integer gameId){
        return (map2DService.getAllEvents(gameId));
    }

}
