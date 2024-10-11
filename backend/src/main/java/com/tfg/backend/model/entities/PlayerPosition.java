package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PlayerPosition", schema = "public")
public class PlayerPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int optaId;
    private int frameId;
    private int period;
    private float x;
    private float y;
    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public PlayerPosition() {
    }

    public PlayerPosition(int optaId, int frameId, int period, float x, float y, Player player, Game game) {
        this.optaId = optaId;
        this.frameId = frameId;
        this.period = period;
        this.x = x;
        this.y = y;
        this.player = player;
        this.game = game;
    }
}
