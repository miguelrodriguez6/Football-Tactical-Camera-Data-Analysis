package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Stats", schema = "public")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String minutes;
    private float distance;
    private float walking;
    private float jogging;
    private float running;
    private float highSpeedRunning;
    private float sprinting;
    private int numberOfHighIntensityRuns;
    private float topSpeed;
    private float averageSpeed;
    private float distanceTip;
    private float hsrDistanceTip;
    private float sprintDistanceTip;
    private int numberOfHighIntensityRunsTip;
    private float distanceOtip;
    private float hsrDistanceOtip;
    private float sprintDistanceOtip;
    private int numberOfHighIntensityRunsOtip;
    private float distanceBop;
    private float hsrDistanceBop;
    private float sprintDistanceBop;
    private int numberOfHighIntensityRunsBop;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;
    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    public Stats() {
    }

    public Stats(String minutes, float distance, float walking, float jogging, float running, float highSpeedRunning, float sprinting, int numberOfHighIntensityRuns, float topSpeed, float averageSpeed, float distanceTip, float hsrDistanceTip, float sprintDistanceTip, int numberOfHighIntensityRunsTip, float distanceOtip, float hsrDistanceOtip, float sprintDistanceOtip, int numberOfHighIntensityRunsOtip, float distanceBop, float hsrDistanceBop, float sprintDistanceBop, int numberOfHighIntensityRunsBop, Game game, Player player) {
        this.minutes = minutes;
        this.distance = distance;
        this.walking = walking;
        this.jogging = jogging;
        this.running = running;
        this.highSpeedRunning = highSpeedRunning;
        this.sprinting = sprinting;
        this.numberOfHighIntensityRuns = numberOfHighIntensityRuns;
        this.topSpeed = topSpeed;
        this.averageSpeed = averageSpeed;
        this.distanceTip = distanceTip;
        this.hsrDistanceTip = hsrDistanceTip;
        this.sprintDistanceTip = sprintDistanceTip;
        this.numberOfHighIntensityRunsTip = numberOfHighIntensityRunsTip;
        this.distanceOtip = distanceOtip;
        this.hsrDistanceOtip = hsrDistanceOtip;
        this.sprintDistanceOtip = sprintDistanceOtip;
        this.numberOfHighIntensityRunsOtip = numberOfHighIntensityRunsOtip;
        this.distanceBop = distanceBop;
        this.hsrDistanceBop = hsrDistanceBop;
        this.sprintDistanceBop = sprintDistanceBop;
        this.numberOfHighIntensityRunsBop = numberOfHighIntensityRunsBop;
        this.game = game;
        this.player = player;
    }
}
