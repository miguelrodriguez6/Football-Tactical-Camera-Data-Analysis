package com.tfg.backend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "PlayerHistory", schema = "public")
public class PlayerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    public PlayerHistory() {
    }

    public PlayerHistory(LocalDate startDate, LocalDate endDate, Player player, Team team) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.player = player;
        this.team = team;
    }
}
