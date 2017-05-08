package com.backbase.kalah.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;
import java.util.UUID;

/**
 * Rich domain model representing an ongoing kalah game
 */
@Entity
public class Game {

    @Id
    @Getter
    @Setter
    private UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private GameState gameState = GameState.WAITING_FOR_PLAYER;

    @Getter
    @Setter
    private Player nextPlayer;

    @Getter
    @Setter
    private Player winningPlayer;

    @Getter
    @Setter
    private Integer[] pits;

    @Getter
    private final Integer pitsPerPlayer;

    @Getter
    private final Integer stonesPerPit;

    public enum Player {
        NORTH, SOUTH; // Initating player will be on NORTH side

        public Player getOpponent() {
            if (this.equals(NORTH)) {
                return Player.SOUTH;
            } else {
                return Player.NORTH;
            }
        }
    }

    public enum GameState {
        WAITING_FOR_PLAYER, RUNNING, FINISHED;
    }

    public Game() { // No-args constructor for serialization
        this(6, 6);
    }

    public Game(Integer pitsPerPlayer, Integer stonesPerPit) {
        pits = new Integer[2 * pitsPerPlayer + 2];
        for (int i = 0; i < 2 * pitsPerPlayer + 2; i++) {
            if ((i != pitsPerPlayer) && (i != 2 * pitsPerPlayer + 1)) {
                pits[i] = stonesPerPit;
            } else {
                pits[i] = 0;
            }
        }
        this.pitsPerPlayer = pitsPerPlayer;
        this.stonesPerPit = stonesPerPit;
    }

    public void playerJoin() throws InvalidMoveException {
        if (gameState != GameState.WAITING_FOR_PLAYER) {
            throw new InvalidMoveException();
        }

        gameState = GameState.RUNNING;
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            nextPlayer = Player.NORTH;
        } else {
            nextPlayer = Player.SOUTH;
        }
    }

    public void sow(Player player, Integer index) throws InvalidMoveException {
        if (player != nextPlayer) {
            throw new InvalidMoveException();
        }

        if (!isOwnPitIndex(player, index)) {
            throw new InvalidMoveException();
        }

        if (pits[index] == 0) {
            throw new InvalidMoveException();
        }

        Integer nrSeedsToSow = pits[index];
        pits[index] = 0;

        Integer playerKalahIndex = getKalahIndex(player);
        Integer opponentKalahIndex = getKalahIndex(player.getOpponent());

        // sow
        while (nrSeedsToSow > 0) {
            index++;
            if (index == opponentKalahIndex) {
                index++; // skip the opponent's kalah
            }
            if (index > 2 * pitsPerPlayer + 1) {
                index = 0;
            }
            pits[index]++;
            nrSeedsToSow--;
        }

        // decide whose round will be the next
        if (index == playerKalahIndex) {
            nextPlayer = player;
        } else {
            nextPlayer = player.getOpponent();
        }

        // capture
        if ((isOwnPitIndex(player, index)) && (pits[index] == 1)
                && (index != playerKalahIndex)) { // when the last stone lands in an own empty pit
            pits[playerKalahIndex]++; // the player captures this stone
            pits[index] = 0;

            Integer oppositePitIndex = getOppositePitIndex(index);
            pits[playerKalahIndex] += pits[oppositePitIndex]; // ... and all stones in the opposite pit
            pits[oppositePitIndex] = 0;
        }

        // check for ending
        if ((sumPlayerStonesNotInKalah(Player.NORTH) == 0)
                || (sumPlayerStonesNotInKalah(Player.SOUTH) == 0)) {
            finish();
            if (pits[playerKalahIndex] >= pits[opponentKalahIndex]) { // current player wins if it's a draw
                gameState = GameState.FINISHED;
                winningPlayer = player;
            } else {
                gameState = GameState.FINISHED;
                winningPlayer = player.getOpponent();
            }
        }
    }

    public void finish() {
        Integer northKalahIndex = getKalahIndex(Player.NORTH);
        Integer southKalahIndex = getKalahIndex(Player.SOUTH);
        for (int i = 0; i < northKalahIndex; i++) {
            pits[northKalahIndex] += pits[i];
            pits[i] = 0;
        }
        for (int i = northKalahIndex + 1; i < southKalahIndex; i++) {
            pits[southKalahIndex] += pits[i];
            pits[i] = 0;
        }
    }

    private Integer getKalahIndex(Player player) {
        if (player.equals(Player.NORTH)) {
            return pitsPerPlayer;
        } else {
            return 2 * pitsPerPlayer + 1;
        }
    }

    private Integer getOppositePitIndex(Integer index) {
        return 2 * pitsPerPlayer - index;
    }

    private boolean isOwnPitIndex(Player player, Integer index) {
        return ((player.equals(Player.NORTH) && index <= pitsPerPlayer) ||
                (player.equals(Player.SOUTH) && index > pitsPerPlayer));
    }

    private Integer sumPlayerStonesNotInKalah(Player player) {
        Integer sum = 0;
        if (player.equals(Player.NORTH)) {
            for (int i = 0; i < pitsPerPlayer; i++) {
                sum += pits[i];
            }
        } else {
            for (int i = pitsPerPlayer + 1; i < 2 * pitsPerPlayer + 1; i++) {
                sum += pits[i];
            }
        }
        return sum;
    }

}