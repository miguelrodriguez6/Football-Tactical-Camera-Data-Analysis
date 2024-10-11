package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Player", schema = "public")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int number;
    private String position;
    private int optaId;

    public Player() {
    }

    public Player(String name, int number, String position, int optaId) {
        this.name = name;
        this.number = number;
        this.position = position;
        this.optaId = optaId;
    }
}
