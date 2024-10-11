package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Team", schema = "public")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int optaId;
    private String name;

    public Team() {
    }

    public Team(int optaId, String name) {
        this.optaId = optaId;
        this.name = name;
    }
}
