package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameDao extends JpaRepository<Game, Long> {
    @Query(value = "select * from game g where g.user_id=:userId", nativeQuery = true)
    List<Game> findAllByUserId(@Param("userId") int userId);
}
