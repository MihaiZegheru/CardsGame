package game;


import game.datacollections.MinionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static utility.Math.clamp;

public class Game {
    private static final int MAX_COLUMNS = 5;

    private Player playerOne;
    private Player playerTwo;
    private int playerAtTurnId;
    private int seed;

    private final int startingPlayerId;
    private int currRound;
    private ArrayList<ArrayList<Minion>> board;

    private int manaRate;

    public Game(Player playerOne, Player playerTwo, int startingPlayerId, int seed) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerAtTurnId = startingPlayerId;
        this.seed = seed;
        this.startingPlayerId = startingPlayerId;
        this.currRound = 0;
        this.manaRate = 0;

        this.board = new ArrayList<>();
        this.board.add(new ArrayList<>());
        this.board.add(new ArrayList<>());
        this.board.add(new ArrayList<>());
        this.board.add(new ArrayList<>());

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

        playerAtTurnId = getNextTurnPlayer();
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

        Minion minion =  new Minion(card);
        switch (ResolveCardType(card)) {
            case DAMAGE_DEALER:
                PlaceMinion(player.getDdRow(), board.get(player.getDdRow()).size(), minion);
                break;
            case TANK:
                PlaceMinion(player.getTankRow(), board.get(player.getTankRow()).size(), minion);
                break;
        }

        return minion;
    }

    private void PlaceMinion(int row, int col, Minion card) {
        if (col >= MAX_COLUMNS) {
            System.out.println("Row already full.");
            return;
        }
        board.get(row).add(card);
    }

    public int getPlayerAtTurnId() { return playerAtTurnId; }

    private int getNextTurnPlayer() { return playerAtTurnId % 2 + 1; }

    public ArrayList<ArrayList<Minion>> getBoard() { return board; }


    private CardType ResolveCardType(MinionData card) {
        return switch (card.getName()) {
            case "Sentinel", "Berserker", "The Cursed One", "Disciple" -> CardType.DAMAGE_DEALER;
            case "Goliath", "Warden", "The Ripper", "Miraj" -> CardType.TANK;
            default -> {
                System.out.println("Card " + card + " implementation does not exist.");
                yield CardType.DEFAULT;
            }
        };
    }
}
