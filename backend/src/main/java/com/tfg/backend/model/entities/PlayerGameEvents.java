package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PlayerGameEvents", schema = "public")
public class PlayerGameEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int opPlayerId;
    private int typeId;
    private int minute;
    private int second;
    private float x;
    private float y;
    private int alignedFrameid;
    private int outcome;
    private String side;
    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public PlayerGameEvents() {
    }

    public PlayerGameEvents(int opPlayerId, int typeId, int minute, int second, float x, float y, int alignedFrameid, int outcome, String side, Player player, Game game) {
        this.opPlayerId = opPlayerId;
        this.typeId = typeId;
        this.minute = minute;
        this.second = second;
        this.x = x;
        this.y = y;
        this.alignedFrameid = alignedFrameid;
        this.outcome = outcome;
        this.side = side;
        this.player = player;
        this.game = game;
    }
}
