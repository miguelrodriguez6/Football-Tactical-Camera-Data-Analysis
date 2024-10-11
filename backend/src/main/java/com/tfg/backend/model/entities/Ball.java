package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Ball", schema = "public")
public class Ball {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int frameId;
    private float x;
    private float y;
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public Ball() {
    }

    public Ball(int frameId, float x, float y, Game game) {
        this.frameId = frameId;
        this.x = x;
        this.y = y;
        this.game = game;
    }
}
