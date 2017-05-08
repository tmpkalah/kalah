package com.backbase.kalah.controller;


import com.backbase.kalah.controller.dto.SowActionDto;
import com.backbase.kalah.domain.InvalidMoveException;
import com.backbase.kalah.domain.Game;
import com.backbase.kalah.service.GameNotFoundException;
import com.backbase.kalah.service.KalahService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/kalah")
@CrossOrigin(origins = "*")
public class KalahController {

    private KalahService kalahService;

    public KalahController(KalahService kalahService) {
        this.kalahService = kalahService;
    }

    @PostMapping("/")
    public ResponseEntity<Game> startGame() {
        Game game = kalahService.startGame();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{gameId}")
                .buildAndExpand(game.getId()).toUri();
        return ResponseEntity.created(location).body(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable UUID gameId) throws GameNotFoundException {
        Game game = kalahService.getGame(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<Game> joinGame(@PathVariable UUID gameId) throws InvalidMoveException, GameNotFoundException {
        Game game = kalahService.joinGame(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/sow")
    public ResponseEntity<Game> sow(@PathVariable UUID gameId, @RequestBody SowActionDto sowActionDto)
            throws InvalidMoveException, GameNotFoundException {
        Game game = kalahService.sow(gameId, sowActionDto.getPlayer(), sowActionDto.getPitIndex());
        return ResponseEntity.ok(game);
    }

}
