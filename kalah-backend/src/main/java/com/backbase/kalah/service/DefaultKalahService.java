package com.backbase.kalah.service;

import com.backbase.kalah.domain.Game;
import com.backbase.kalah.domain.InvalidMoveException;
import com.backbase.kalah.repository.KalahRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service
public class DefaultKalahService implements KalahService {

    private final static Integer PITS_PER_PLAYER = 6;
    private final static Integer STONES_PER_PIT = 6;

    private KalahRepository kalahRepository;

    @Inject
    public DefaultKalahService(KalahRepository kalahRepository) {
        this.kalahRepository = kalahRepository;
    }

    @Override
    public Game startGame() {
        Game game = new Game(PITS_PER_PLAYER, STONES_PER_PIT);
        kalahRepository.save(game);
        return game;
    }

    @Override
    public Game sow(UUID gameId, Game.Player player, Integer index) throws InvalidMoveException, GameNotFoundException {
        Game game = kalahRepository.findOne(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }
        game.sow(player, index);
        kalahRepository.save(game);
        return game;
    }

    @Override
    public Game joinGame(UUID gameId) throws InvalidMoveException, GameNotFoundException {
        Game game = kalahRepository.findOne(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }
        game.playerJoin();
        kalahRepository.save(game);
        return game;
    }

    @Override
    public Game getGame(UUID gameId) throws GameNotFoundException {
        Game game = kalahRepository.findOne(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }
        return game;
    }

}
