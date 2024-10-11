package com.tfg.backend.model.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfg.backend.model.entities.Game;

import java.util.List;
import java.util.Optional;

public interface GamesService {
    List<Game> getGamesByUser(int userId);
    void saveMetadata(JsonNode node, Long userId, int gameId);
    void saveData(JsonNode node, Long userId, int gameId);
    void saveInsight(JsonNode node, Long userId, int gameId);
    void savePS(String[] line, int gameId);
    Game getGame(int gameId);
    void modifyGameVideoUrl(String filename, Integer gameId);
}
