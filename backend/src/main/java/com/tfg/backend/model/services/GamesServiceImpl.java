package com.tfg.backend.model.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfg.backend.model.entities.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class GamesServiceImpl implements GamesService{

    @Autowired
    GameDao gameDao;
    @Autowired
    TeamDao teamDao;
    @Autowired
    UserDao userDao;
    @Autowired
    PlayerDao playerDao;
    @Autowired
    PlayerHistoryDao playerHistoryDao;
    @Autowired
    GameFrameDao gameFrameDao;
    @Autowired
    PlayerGameEventsDao playerGameEventsDao;
    @Autowired
    StatsDao statsDao;
    @Autowired
    PlayerPositionDao playerPositionDao;
    @Autowired
    BallDao ballDao;

    @Override
    public List<Game> getGamesByUser(int userId) {
        Optional<User> user = userDao.findById((long) userId);
        if (user.isPresent()){
            if (user.get().getRole()== User.RoleType.ADMIN){
                return gameDao.findAll();
            }
        }
        return gameDao.findAllByUserId(userId);
    }

    @Override
    public void saveMetadata(JsonNode node, Long userId, int gameId) {
        String description = node.get("description").asText();
        String result = node.get("homeScore").asInt() + "-" + node.get("awayScore").asInt();
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(node.get("startTime").asLong()), ZoneId.systemDefault());
        User user = null;
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        String[] parts = node.get("description").asText().split(" - ");
        Team homeTeam = teamDao.findByOptaId(node.get("homeOptaId").asInt());
        if (homeTeam==null){
            if (parts.length > 1) {
                String teamName = parts[0];
                Team newHomeTeam = new Team(node.get("homeOptaId").asInt(), teamName);
                teamDao.save(newHomeTeam);
                homeTeam = newHomeTeam;
            }
        }
        Team awayTeam = teamDao.findByOptaId(node.get("awayOptaId").asInt());
        if (awayTeam==null){
            if (parts.length > 1) {
                String teamName = parts[1].split(" : ")[0];
                Team newAwayTeam = new Team(node.get("awayOptaId").asInt(), teamName);
                teamDao.save(newAwayTeam);
                awayTeam = newAwayTeam;
            }
        }
        double pitchLength = node.get("pitchLength").asDouble();
        double pitchWidth = node.get("pitchWidth").asDouble();
        Game game = new Game((long) gameId, description, result, startTime, null, pitchLength, pitchWidth, user, homeTeam, awayTeam);
        gameDao.save(game);

        JsonNode homePlayersList = node.get("homePlayers");
        int i = 0;
        while(homePlayersList.get(i)!=null){
            int optaId = homePlayersList.get(i).get("optaId").asInt();
            if (playerDao.findByOptaId(optaId)==null){
                String name = homePlayersList.get(i).get("name").asText();
                int number = homePlayersList.get(i).get("number").asInt();
                String position = homePlayersList.get(i).get("position").asText();
                Player player = new Player(name, number, position, optaId);
                playerDao.save(player);
                Player playerH = playerDao.findByOptaId(optaId);
                PlayerHistory playerHistory = new PlayerHistory(startTime.toLocalDate(), null, playerH, homeTeam);
                playerHistoryDao.save(playerHistory);
            } else {
                Player player = playerDao.findByOptaId(optaId);
                Optional<PlayerHistory> playerHistoryOptional = playerHistoryDao.findById(player.getId());
                if (playerHistoryOptional.isPresent() && homeTeam!=playerHistoryDao.findById(player.getId()).get().getTeam()){
                    PlayerHistory playerHistory = new PlayerHistory(startTime.toLocalDate(), null, player, homeTeam);
                    playerHistoryDao.save(playerHistory);
                    Optional<PlayerHistory> oldPlayerHistory = playerHistoryDao.findById(player.getId());
                    if (oldPlayerHistory.isPresent()){
                        oldPlayerHistory.get().setEndDate(LocalDate.from(startTime.minusDays(1)));
                        playerHistoryDao.save(oldPlayerHistory.get());
                    }
                }
            }
            i++;
        }

        JsonNode awayPlayersList = node.get("awayPlayers");
        int j = 0;
        while(awayPlayersList.get(j)!=null){
            int optaId = awayPlayersList.get(j).get("optaId").asInt();
            if (playerDao.findByOptaId(optaId)==null){
                String name = awayPlayersList.get(j).get("name").asText();
                int number = awayPlayersList.get(j).get("number").asInt();
                String position = awayPlayersList.get(j).get("position").asText();
                Player player = new Player(name, number, position, optaId);
                playerDao.save(player);
                Player playerA = playerDao.findByOptaId(optaId);
                PlayerHistory playerHistory = new PlayerHistory(startTime.toLocalDate(), null, playerA, awayTeam);
                playerHistoryDao.save(playerHistory);
            } else {
                Player player = playerDao.findByOptaId(optaId);
                Optional<PlayerHistory> playerHistoryOptional = playerHistoryDao.findById(player.getId());
                if (playerHistoryOptional.isPresent() && awayTeam!=playerHistoryDao.findById(player.getId()).get().getTeam()){
                    PlayerHistory playerHistory = new PlayerHistory(startTime.toLocalDate(), null, player, awayTeam);
                    playerHistoryDao.save(playerHistory);
                    Optional<PlayerHistory> oldPlayerHistory = playerHistoryDao.findById(player.getId());
                    if (oldPlayerHistory.isPresent()){
                        oldPlayerHistory.get().setEndDate(LocalDate.from(startTime.minusDays(1)));
                        playerHistoryDao.save(oldPlayerHistory.get());
                    }
                }
            }
            j++;
        }
    }

    @Override
    public void saveData(JsonNode node, Long userId, int gameId) {
        int frameId = node.get("frameIdx").asInt();
        System.out.println("FrameId: " + frameId);
        int period = node.get("period").asInt();
        float gameClock = node.get("gameClock").floatValue();
        boolean live = node.get("live").asBoolean();
        String lastTouch = node.get("lastTouch").asText();
        float ballSpeed = (float) node.get("ball").get("speed").asDouble();

        JsonNode ballXyzArray = node.get("ball").get("xyz");
        double ballX = ballXyzArray.get(0).asDouble();
        double ballY = ballXyzArray.get(1).asDouble();
        Optional<Game> game = gameDao.findById((long) gameId);
        if (game.isPresent()){
            Ball ball = new Ball(frameId, (float) ballX, (float) ballY, game.get());
            ballDao.save(ball);
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(ballX, ballY);
        Geometry geometryBall = geometryFactory.createPoint(coordinate);

        JsonNode homePlayersList = node.get("homePlayers");
        JsonNode playerNode = homePlayersList.get(0);
        int i = 1;

        while(playerNode!=null){
            String playerLargeId = playerNode.get("playerId").asText();
            int number = playerNode.get("number").asInt();
            float speed = (float) playerNode.get("speed").asDouble();
            String side = "home";
            JsonNode playerXyzArray = playerNode.get("xyz");
            double playerX = playerXyzArray.get(0).asDouble();
            double playerY = playerXyzArray.get(1).asDouble();
            int optaId = playerNode.get("optaId").asInt();

            Player player = playerDao.findByOptaId(optaId);

            GeometryFactory geometryFactoryPlayer = new GeometryFactory();
            Coordinate coordinatePlayer = new Coordinate(playerX, playerY);
            Geometry geometryPlayer = geometryFactoryPlayer.createPoint(coordinatePlayer);

            if (gameDao.findById((long) gameId).isPresent()){
                GameFrame gameFrame = new GameFrame(frameId, period, gameClock, live, lastTouch, ballSpeed, playerLargeId, number, speed, side, geometryPlayer, geometryBall, optaId, player, gameDao.findById((long) gameId).get());
                gameFrameDao.save(gameFrame);
                PlayerPosition playerPosition = new PlayerPosition(optaId, frameId, period, (float) playerX, (float) playerY, player, game.get());
                playerPositionDao.save(playerPosition);
            }
            playerNode = homePlayersList.get(i);
            i++;
        }

        JsonNode awayPlayersList = node.get("awayPlayers");
        playerNode = awayPlayersList.get(0);
        i = 1;
        while(playerNode!=null){

            String playerLargeId = playerNode.get("playerId").asText();
            int number = playerNode.get("number").asInt();
            float speed = (float) playerNode.get("speed").asDouble();
            String side = "away";

            JsonNode playerXyzArray = playerNode.get("xyz");
            double playerX = playerXyzArray.get(0).asDouble();
            double playerY = playerXyzArray.get(1).asDouble();
            int optaId = playerNode.get("optaId").asInt();

            Player player = playerDao.findByOptaId(optaId);

            GeometryFactory geometryFactoryPlayer = new GeometryFactory();
            Coordinate coordinatePlayer = new Coordinate(playerX, playerY);
            Geometry geometryPlayer = geometryFactoryPlayer.createPoint(coordinatePlayer);

            if (gameDao.findById((long) gameId).isPresent()){
                GameFrame gameFrame = new GameFrame(frameId, period, gameClock, live, lastTouch, ballSpeed, playerLargeId, number, speed, side, geometryPlayer, geometryBall, optaId, playerDao.findByOptaId(playerNode.get("optaId").asInt()), gameDao.findById((long) gameId).get());
                gameFrameDao.save(gameFrame);
                PlayerPosition playerPosition = new PlayerPosition(optaId, frameId, period, (float) playerX, (float) playerY, player, game.get());
                playerPositionDao.save(playerPosition);
            }
            playerNode = awayPlayersList.get(i);
            i++;
        }
    }

    @Override
    public void saveInsight(JsonNode node, Long userId, int gameId) {

        if (node.has("optaEvent")){
            int typeId = 0;
            if (node.get("optaEvent").has("typeId") && node.get("optaEvent").has("outcome")){
                if (node.get("optaEvent").get("outcome").asInt()==1){
                    typeId = node.get("optaEvent").get("typeId").asInt();
                }
            }
            switch (typeId) {
                case 2:
                case 4:
                case 6:
                case 16:
                case 17:
                case 18:
                case 19:

                    int opPlayerId = 0;
                    if (node.get("optaEvent").has("opPlayerId")){
                        opPlayerId = Integer.parseInt(node.get("optaEvent").get("opPlayerId").asText());
                    } else {
                        break;
                    }
                    int minute = node.get("optaEvent").get("timeMin").asInt();
                    int alignedFrameId = node.get("optaEvent").get("alignedFrameIdx").asInt();
                    int outcome = node.get("optaEvent").get("outcome").asInt();
                    int second = node.get("optaEvent").get("timeSec").asInt();
                    String side = null;
                    if (typeId==6){
                        JsonNode qualifierList = node.get("optaEvent").get("qualifier");
                        for (JsonNode item : qualifierList) {
                            if (item.has("qualifierId")) {
                                int qualifierId = item.get("qualifierId").asInt();
                                if (qualifierId == 73) {
                                    side = "left";
                                } else if (qualifierId == 75){
                                    side = "right";
                                }
                            }
                        }
                    }

                    float x = (float) node.get("optaEvent").get("x").asDouble();
                    float y = (float) node.get("optaEvent").get("y").asDouble();
                    Optional<Game> game = gameDao.findById((long) gameId);
                    Player player = playerDao.findByOptaId(opPlayerId);
                    if (game.isPresent()){
                        PlayerGameEvents playerGameEvents = new PlayerGameEvents(opPlayerId, typeId, minute, second, x, y, alignedFrameId, outcome, side, player, game.get());
                        playerGameEventsDao.save(playerGameEvents);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void savePS(String[] line, int gameId) {
        Optional<Game> game = gameDao.findById((long) gameId);
        Player player = playerDao.findByOptaId(Integer.parseInt(line[0]));
        Stats stats = new Stats(line[2], Float.parseFloat(line[3]), Float.parseFloat(line[4]), Float.parseFloat(line[5]), Float.parseFloat(line[6]), Float.parseFloat(line[7]), Float.parseFloat(line[8]), Integer.parseInt(line[9]), Float.parseFloat(line[10]), Float.parseFloat(line[11]), Float.parseFloat(line[12]), Float.parseFloat(line[13]), Float.parseFloat(line[14]), Integer.parseInt(line[15]), Float.parseFloat(line[16]), Float.parseFloat(line[17]), Float.parseFloat(line[18]), Integer.parseInt(line[19]), Float.parseFloat(line[20]), Float.parseFloat(line[21]), Float.parseFloat(line[22]), Integer.parseInt(line[23]), game.get(), player);
        statsDao.save(stats);
    }

    @Override
    public Game getGame(int gameId) {
        Optional<Game> game = gameDao.findById((long) gameId);
        return game.orElse(null);
    }

    @Override
    public void modifyGameVideoUrl(String filename, Integer gameId){
        Optional<Game> game = gameDao.findById((long) gameId);
        if (game.isPresent()){
            game.get().setVideoUrl(filename);
            gameDao.save(game.get());
        }
    }
}
