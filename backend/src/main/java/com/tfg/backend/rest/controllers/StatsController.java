package com.tfg.backend.rest.controllers;

import com.tfg.backend.model.entities.Stats;
import com.tfg.backend.model.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/stats")
public class StatsController {



    @Autowired
    StatsService statsService;

    @GetMapping("/graph")
    public String getRadarGraph(@RequestParam("side") String side, @RequestParam("gameId") Integer gameId){
        return statsService.getStats(side, gameId);
    }

    @GetMapping("/possession")
    public Object[] getPossession(@RequestParam("gameId") Integer gameId){
        return statsService.getPossession(gameId);
    }

    @GetMapping("/team-distance")
    public Integer getDistance(@RequestParam("gameId") Integer gameId, @RequestParam("teamOptaId") Integer teamOptaId){
        return statsService.getDistance(gameId, teamOptaId);
    }

    @GetMapping("/team-stats")
    public Object[] getTeamStats(@RequestParam("gameId") int gameId, @RequestParam("teamOptaId") int teamOptaId){
        return statsService.getTeamStats(gameId, teamOptaId);
    }

    @GetMapping("/top-speed")
    public List<Stats> getTopSpeed(@RequestParam("gameId") int gameId){
        return statsService.getTopThreeByTopSpeedAndGameId(gameId);
    }

    @GetMapping("/player-stats")
    public Stats getPlayerStatsByGame(@RequestParam("gameId") int gameId, @RequestParam("playerOptaId") int playerOptaId){
        return statsService.getPlayerStatsByGameId(gameId, playerOptaId);
    }
}
