package com.tfg.backend.rest.controllers;

import com.tfg.backend.model.services.PlayService;
import com.tfg.backend.model.services.PlayerClip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/jugadas")
public class PlayController {

    @Autowired
    PlayService playService;

    @GetMapping("/desmarque_ruptura")
    public List<PlayerClip> desmarqueEnRuptura(@RequestParam("side") String side, @RequestParam("gameId") Integer gameId){
        return playService.desmarqueEnRuptura(side, gameId);
    }

    @GetMapping("/desmarque_apoyo")
    public List<PlayerClip> desmarqueEnApoyo(@RequestParam("side") String side, @RequestParam("gameId") Integer gameId){
        return playService.desmarqueEnApoyo(side, gameId);
    }

    @GetMapping("/ataque_banda")
    public List<PlayerClip> ataqueBanda(@RequestParam("side") String side, @RequestParam("gameId") Integer gameId){
        return playService.ataquePorBanda(side, gameId);
    }

    @GetMapping("contraataque")
    public List<PlayerClip> contraataque(@RequestParam("side") String side, @RequestParam("gameId") Integer gameId){
        return playService.contraataque(side, gameId);
    }

}
