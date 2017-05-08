package com.backbase.kalah.repository;

import com.backbase.kalah.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KalahRepository extends JpaRepository<Game, UUID> {

}
