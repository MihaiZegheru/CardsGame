package game;


import fileio.Coordinates;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static utility.Math.Clamp;

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
        // TODO: Reset card activation

        playerAtTurnId = getNextTurnPlayer();
        if (playerAtTurnId == startingPlayerId) {
            StartRound();
        }
    }

    private void StartRound() {
        currRound++;
        manaRate = Clamp(manaRate + 1, 0, 10);
        playerOne.DrawCard();
        playerTwo.DrawCard();
        playerOne.IncreaseCurrMana(manaRate);
        playerTwo.IncreaseCurrMana(manaRate);
    }

    public Status AttackAt(Player attacker, Minion minion, Coordinates defenderCoords) {
        Player defender;
        if (attacker == playerOne) {
            defender = playerTwo;
        } else {
            defender = playerOne;
        }

        StatusOr<Minion> defenderMinionStatus = defender.getArmy().getMinionAt(defenderCoords);
        if (!defenderMinionStatus.isOk()) {
            return defenderMinionStatus;
        }

        Minion defenderMinion = defenderMinionStatus.getBody();
        if (defender.getArmy().hasTanks() && !defenderMinion.isTank()) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank’.");
        }
        minion.Attack(defenderMinion);
        return Status.ok();
    }

    public void OnMinionDeath(Minion minion) {
        for (ArrayList<Minion> row : board) {
            if (row.remove(minion)) {
                return;
            }
        }
    }

    public int getPlayerAtTurnId() { return playerAtTurnId; }

    private int getNextTurnPlayer() { return playerAtTurnId % 2 + 1; }

    public ArrayList<ArrayList<Minion>> getBoard() {
        ArrayList<ArrayList<Minion>> board = new ArrayList<>();
        board.add(playerTwo.getArmy().getDdLane());
        board.add(playerTwo.getArmy().getTankLane());
        board.add(playerOne.getArmy().getTankLane());
        board.add(playerOne.getArmy().getDdLane());
        return board;
    }
}
