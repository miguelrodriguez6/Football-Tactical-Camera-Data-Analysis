package com.tfg.backend.rest.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.tfg.backend.model.entities.Game;
import com.tfg.backend.model.services.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/games")
public class GamesController {

    @Autowired
    GamesService gamesService;

    @GetMapping("/all")
    public List<Game> retrieveGamesByUser(@RequestParam("userId") int userId){
        return gamesService.getGamesByUser(userId);
    }

    @PostMapping("/upload/data")
    public String uploadData(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("gameId") int gameId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ExecutorService executor = Executors.newFixedThreadPool(10);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String jsonLine = line;
                    executor.submit(() -> {
                        try {
                            JsonNode jsonNode = objectMapper.readTree(jsonLine);
                            gamesService.saveData(jsonNode, userId, gameId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            executor.shutdown();
            return "Archivo subido correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al subir el archivo";
        }
    }

    @PostMapping("/upload/insight")
    public String uploadInsight(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("gameId") int gameId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    JsonNode jsonNode = objectMapper.readTree(line);
                    gamesService.saveInsight(jsonNode, userId, gameId);
                }
            }
            return "Archivo subido correctamente";
        } catch (Exception e) {
            return "Error al subir el archivo";
        }
    }

    @PostMapping("/upload/metadata")
    public String uploadMetadata(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("gameId") int gameId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file.getBytes());
            gamesService.saveMetadata(rootNode, userId, gameId);
            return "Archivo subido correctamente";
        } catch (Exception e) {
            return "Error al subir el archivo";
        }
    }

    @PostMapping("/upload/summary")
    public String uploadPhysicalSummary(@RequestParam("file") MultipartFile file, @RequestParam("gameId") int gameId) {
        try {
            Reader reader = new InputStreamReader(file.getInputStream());

            CSVReader csvReader = new CSVReader(reader);
            List<String[]> lines = csvReader.readAll();
            boolean columnNames = true;
            for (String[] line : lines) {
                if (!columnNames){
                    gamesService.savePS(line, gameId);
                }
                columnNames = false;
            }
            csvReader.close();
            reader.close();
            return "Archivo subido correctamente";
        } catch (Exception e) {
            return "Error al subir el archivo";
        }
    }

    @PostMapping("/upload/video")
    public ResponseEntity<Object> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("gameId") int gameId) {
        gamesService.modifyGameVideoUrl(file.getOriginalFilename(), gameId);
        try {
            File path = new File("C:\\Users\\migue\\tfg\\proyecto\\tfg\\frontend\\src\\assets\\media\\video\\" + file.getOriginalFilename());
            path.createNewFile();
            FileOutputStream output = new FileOutputStream(path);
            output.write(file.getBytes());
            output.close();
            return ResponseEntity.status(HttpStatus.OK).body("Successfully uploaded!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo.");
        }
    }

    @GetMapping("/game")
    public Game retrieveGameInfo(@RequestParam("gameId") int gameId){
        return gamesService.getGame(gameId);
    }
}
