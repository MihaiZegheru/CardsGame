package game;


import fileio.Coordinates;
import game.datacollections.MinionData;
import utility.Status;

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

    public Status AttackFromAt(Player attacker, Coordinates atkrCoords, Coordinates atkdCoords) {
        // TODO: Add tank as frontline. This should be refactored such that the player owns these cards and this stays
        // the middle man
        Player attacked;
        if (attacker == playerOne) {
            attacked = playerTwo;
        } else {
            attacked = playerOne;
        }

        if (!attacker.OwnsCoords(atkrCoords)) {
            return Status.notOwnAttackerCard();
        }
        if (!attacked.OwnsCoords(atkdCoords)) {
            return Status.ownsAttackedCard();
        }

        System.out.println(board);
        Minion attackerCard = GetCardAtCoords(atkrCoords);
        if (attackerCard == null) {
            return Status.aborted();
        }
        if (attackerCard.getHasAttacked()) {
            return Status.cardHasAttacked();
        }
        if (attackerCard.getIsFrozen()) {
            return Status.cardIsFrozen();
        }

        Minion attackedCard = GetCardAtCoords(atkdCoords);
        if (attackedCard == null) {
            return Status.aborted();
        }

        attackedCard.OnAttacked(attackerCard.getAttackDamage());
        return Status.ok();
    }

    public void OnMinionDeath(Minion minion) {
        for (ArrayList<Minion> row : board) {
            if (row.remove(minion)) {
                return;
            }
        }
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

    private Minion GetCardAtCoords(Coordinates coords) {
        if (board.size() <= coords.getX() || board.get(coords.getX()).size() <= coords.getY()) {
            return null;
        }
        return board.get(coords.getX()).get(coords.getY());
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
