package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "GameFrame", schema = "public")
public class GameFrame implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int frameId;
    private int period;
    private float gameClock;
    private boolean live;
    private String lastTouch;
    private float ballSpeed;
    private String playerLargeId;
    private int number;
    private float speed;
    private String teamSide;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Geometry playerGeom;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Geometry ballGeom;
    private int optaId;
    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public GameFrame() {
    }

    public GameFrame(int frameId, int period, float gameClock, boolean live, String lastTouch, float ballSpeed, String playerLargeId, int number, float speed, String teamSide, Geometry playerGeom, Geometry ballGeom, int optaId, Player player, Game game) {
        this.frameId = frameId;
        this.period = period;
        this.gameClock = gameClock;
        this.live = live;
        this.lastTouch = lastTouch;
        this.ballSpeed = ballSpeed;
        this.playerLargeId = playerLargeId;
        this.number = number;
        this.speed = speed;
        this.teamSide = teamSide;
        this.playerGeom = playerGeom;
        this.ballGeom = ballGeom;
        this.optaId = optaId;
        this.player = player;
        this.game = game;
    }
}
