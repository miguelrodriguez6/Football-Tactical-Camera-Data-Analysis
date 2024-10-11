package com.tfg.backend.rest.controllers;

import com.tfg.backend.model.entities.Player;
import com.tfg.backend.model.services.PlayerDataService;
import com.tfg.backend.rest.dtos.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tfg.backend.rest.dtos.PlayerConverter.toPlayerDto;
import static com.tfg.backend.rest.dtos.PlayerConverter.toPlayersDto;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/jugadores")
public class PlayerMetadataController {

    @Autowired
    PlayerDataService playerDataService;

    @GetMapping("/all")
    public List<PlayerDto> getPlayers(){
        return toPlayersDto(playerDataService.getPlayers());
    }

    @GetMapping("/{id}")
    public PlayerDto getPlayerById(@PathVariable Long id){
        if (playerDataService.getPlayerById(id).isPresent()){
            return toPlayerDto(playerDataService.getPlayerById(id).get());
        }
        return null;
    }

    @GetMapping("/optaId/{id}")
    public PlayerDto getPlayerByOptaId(@PathVariable Integer id){
        return toPlayerDto(playerDataService.getPlayerByOptaId(id));
    }

    @GetMapping("/local/{gameId}")
    public List<PlayerDto> getHomeTeamPlayers(@PathVariable int gameId){
        return toPlayersDto(playerDataService.getHomeTeamPlayers(gameId));
    }

    @GetMapping("/visitante/{gameId}")
    public List<PlayerDto> getAwayTeamPlayers(@PathVariable int gameId){
        return toPlayersDto(playerDataService.getAwayTeamPlayers(gameId));
    }

    @GetMapping("/visitantes/{gameId}")
    public List<Player> getAwayTeamPlayerList(@PathVariable int gameId){
        return playerDataService.getAwayTeamPlayers(gameId);
    }

}
