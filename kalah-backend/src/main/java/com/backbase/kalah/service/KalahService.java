package com.backbase.kalah.service;

import com.backbase.kalah.domain.Game;
import com.backbase.kalah.domain.InvalidMoveException;

import java.util.UUID;

public interface KalahService {
    Game startGame();
    Game sow(UUID gameId, Game.Player player, Integer index) throws InvalidMoveException, GameNotFoundException;
    Game joinGame(UUID gameId) throws InvalidMoveException, GameNotFoundException;
    Game getGame(UUID gameId) throws GameNotFoundException;
}
