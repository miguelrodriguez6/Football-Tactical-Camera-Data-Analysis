package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Game", schema = "public")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String result;
    private LocalDateTime startTime;
    private String videoUrl;
    private double pitchLength;
    private double pitchWidth;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "homeTeamId")
    private Team homeTeam;
    @ManyToOne
    @JoinColumn(name = "awayTeamId")
    private Team awayTeam;

    public Game() {
    }

    public Game(String description, String result, LocalDateTime startTime, String videoUrl, double pitchLength, double pitchWidth, User user, Team homeTeam, Team awayTeam) {
        this.description = description;
        this.result = result;
        this.startTime = startTime;
        this.videoUrl = videoUrl;
        this.pitchLength = pitchLength;
        this.pitchWidth = pitchWidth;
        this.user = user;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Game(Long id, String description, String result, LocalDateTime startTime, String videoUrl, double pitchLength, double pitchWidth, User user, Team homeTeam, Team awayTeam) {
        this.id = id;
        this.description = description;
        this.result = result;
        this.startTime = startTime;
        this.videoUrl = videoUrl;
        this.pitchLength = pitchLength;
        this.pitchWidth = pitchWidth;
        this.user = user;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }
}
