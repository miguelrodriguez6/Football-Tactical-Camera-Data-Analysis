package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDao extends JpaRepository<Team, Long> {
    Team findByOptaId(int optaId);
}
