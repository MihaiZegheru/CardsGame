package game;


import game.datacollections.MinionData;

import java.util.Collections;
import java.util.Random;

import static utility.Math.clamp;

public class Game {
    private Player playerOne;
    private Player playerTwo;
    private int playerAtTurnId;
    private int seed;

    private final int startingPlayerId;
    private int currRound;

    private int manaRate;

    public Game(Player playerOne, Player playerTwo, int startingPlayerId, int seed) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerAtTurnId = startingPlayerId;
        this.seed = seed;
        this.startingPlayerId = startingPlayerId;
        this.currRound = 0;
        this.manaRate = 0;

        Collections.shuffle(playerOne.getDeckCardsData(), new Random(seed));
        Collections.shuffle(playerTwo.getDeckCardsData(), new Random(seed));

        StartRound();
    }

    public void OnEndPlayerTurn(Player caller) {
        if (caller.getId() != playerAtTurnId) {
            System.out.println("Player not at turn to end round.");
            return;
        }

        // TODO: Unfreeze cards of caller

        playerAtTurnId = GetNextTurnPlayer();
        if (playerAtTurnId == startingPlayerId) {
            StartRound();
        }
    }

    private void StartRound() {
        currRound++;
        manaRate = clamp(manaRate + 1, 0, 10);
        playerOne.DrawCard();
        playerTwo.DrawCard();
        playerOne.IncreaseCurrMana(manaRate);
        playerTwo.IncreaseCurrMana(manaRate);
    }

    public Minion PlaceCard(Player player, MinionData card) {
        if (player.getId() != playerAtTurnId) {
            System.out.println("Player not at turn to end round.");
            return null;
        }

        // TODO: Add card to board grid

        return new Minion(card);
    }

    public int getPlayerAtTurnId() { return playerAtTurnId; }

    private int GetNextTurnPlayer() { return playerAtTurnId % 2 + 1; }
}
