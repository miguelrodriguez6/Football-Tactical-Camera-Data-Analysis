package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerHistoryDao extends JpaRepository<PlayerHistory, Long> {
}
