package com.backbase.kalah.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class GameUnitTest {

    @Test
    public void testTotalNumberOfStonesOnCreation() {
        Game game = new Game(6, 6);

        assertThat(game.getPits()).isNotNull();
        assertThat(game.getPits()).doesNotContainNull();

        Integer totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);
    }

    @Test
    public void testTotalNumberOfStonesOnFinish() {
        Game game = new Game(6, 6);
        game.finish();

        assertThat(game.getPits()).isNotNull();
        assertThat(game.getPits()).doesNotContainNull();

        Integer totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);
    }

    @Test
    public void playerPitsShouldBeEmptyOnFinish() {
        Game game = new Game(6, 6);
        game.finish();

        Integer northPlayerStonesInPits = Arrays.asList(game.getPits()).subList(0, 6)
                .stream().mapToInt(Integer::intValue).sum();
        assertThat(northPlayerStonesInPits).isEqualTo(0);
        Integer southPlayerStonesInPits = Arrays.asList(game.getPits()).subList(7, 13)
                .stream().mapToInt(Integer::intValue).sum();
        assertThat(southPlayerStonesInPits).isEqualTo(0);
    }

    @Test
    public void playersShouldTakeTurns() throws InvalidMoveException {
        Game game = new Game(6, 6);
        game.playerJoin();
        game.setNextPlayer(Game.Player.NORTH);

        Game.Player lastPlayer = game.getNextPlayer();
        game.sow(lastPlayer, 0);
        assertThat(game.getNextPlayer()).isEqualTo(lastPlayer); // player gets additional move

        lastPlayer = game.getNextPlayer();
        game.sow(lastPlayer, 2);
        assertThat(game.getNextPlayer()).isNotEqualTo(lastPlayer);

        lastPlayer = game.getNextPlayer();
        game.sow(lastPlayer, 9);
        assertThat(game.getNextPlayer()).isNotEqualTo(lastPlayer);

        lastPlayer = game.getNextPlayer();
        game.sow(lastPlayer, 3);
        assertThat(game.getNextPlayer()).isNotEqualTo(lastPlayer);
    }

    @Test
    public void testGameStateInvariantViolations() throws InvalidMoveException {
        Game game = new Game(6, 6);

        // trying to sow while the game is not started
        assertThatThrownBy(() -> {
            game.sow(Game.Player.NORTH, 2);
        }).isInstanceOf(InvalidMoveException.class);

        // trying to join an already started game
        game.playerJoin();
        assertThatThrownBy(() -> {
            game.playerJoin();
        }).isInstanceOf(InvalidMoveException.class);

        // trying to sow when not player's turn
        game.setNextPlayer(Game.Player.SOUTH);
        assertThatThrownBy(() -> {
            game.sow(Game.Player.NORTH, 3);
        }).isInstanceOf(InvalidMoveException.class);

        // trying to sow from an opponents pit
        assertThatThrownBy(() -> {
            game.sow(Game.Player.SOUTH, 3);
        }).isInstanceOf(InvalidMoveException.class);

        // trying to sow from an empty pit
        game.sow(Game.Player.SOUTH, 7); // this move will finish at player's kalah
        assertThatThrownBy(() -> {
            game.sow(Game.Player.SOUTH, 7);
        }).isInstanceOf(InvalidMoveException.class);
    }

    @Test
    public void testSowing() throws InvalidMoveException {
        // test scenario from wikipedia; we don't care about the `stonesPerPit` parameters here
        Game game = new Game(6, 0);
        game.playerJoin();
        game.setNextPlayer(Game.Player.NORTH);
        game.setPits(new Integer[] {4, 3, 0, 1, 2, 2, 0, 5, 3, 2, 1, 2, 0, 0});
        game.sow(Game.Player.NORTH, 4);
        assertThat(game.getPits()).isEqualTo(new Integer[] {4, 3, 0, 1, 0, 3, 1, 5, 3, 2, 1, 2, 0, 0});
        game.sow(Game.Player.NORTH, 0);
        assertThat(game.getPits()).isEqualTo(new Integer[] {0, 4, 1, 2, 0, 3, 5, 5, 0, 2, 1, 2, 0, 0});
    }

    @Test
    public void sowingShouldNotChangeTotalNumberOfSeeds() throws InvalidMoveException {
        Game game = new Game(6, 6);
        game.playerJoin();
        game.setNextPlayer(Game.Player.NORTH);

        game.sow(Game.Player.NORTH, 2);
        Integer totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);

        game.sow(Game.Player.SOUTH, 8);
        totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);

        game.sow(Game.Player.NORTH, 0);
        totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);

        game.sow(Game.Player.SOUTH, 11);
        totalNumberOfStones = Arrays.asList(game.getPits()).stream().mapToInt(Integer::intValue).sum();
        assertThat(totalNumberOfStones).isEqualTo(2 * 6 * 6);
    }

}
