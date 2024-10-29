package game;

import fileio.Coordinates;
import game.datacollections.DeckData;
import game.datacollections.PlayerData;
import status.Status;
import status.StatusCode;

public class GameManager {
    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private final int cardsPerRow = 5;

    private Game game;
    private Player playerOne;
    private Player playerTwo;

    public void StartGame(PlayerData playerOneData, int playerOneDeckIdx, PlayerData playerTwoData,
                          int playerTwoDeckIdx, int startingPlayerId, int seed) {
        DeckData playerOneDeck = playerOneData.getDecks().get(playerOneDeckIdx);
        this.playerOne = new Player(playerOneData, playerOneDeck, 1);

        DeckData playerTwoDeck = playerTwoData.getDecks().get(playerTwoDeckIdx);
        this.playerTwo = new Player(playerTwoData, playerTwoDeck, 2);

        this.game = new Game(playerOne, playerTwo, startingPlayerId, seed);
    }

    // Can return null
    public Player getPlayer(int id) {
        if (id == 1) {
            return  playerOne;
        } else if (id == 2) {
            return  playerTwo;
        }
        System.out.println("Player with id " + id + " is null.");
        System.exit(-1);
        return null;
    }

    public void EndPlayerTurn() { getPlayer(game.getPlayerAtTurnId()).EndTurn(); }

    public Status PlaceCard(int idx) { return getPlayer(game.getPlayerAtTurnId()).PlaceCard(idx); }

    public Status UseMinionAttack(Coordinates attackerCoords, Coordinates defenderCoords) {
        if (game.getPlayerAtTurnId() == 1 && ((attackerCoords.getX() != 2 && attackerCoords.getX() != 3) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        } else if (game.getPlayerAtTurnId() == 2 && ((attackerCoords.getX() != 0 && attackerCoords.getX() != 1) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        }
        if (game.getPlayerAtTurnId() == 1 && ((defenderCoords.getX() != 0 && defenderCoords.getX() != 1) ||
                defenderCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
        } else if (game.getPlayerAtTurnId() == 2 && ((defenderCoords.getX() != 2 && defenderCoords.getX() != 3) ||
                defenderCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
        }
        return getPlayer(game.getPlayerAtTurnId()).UseMinionAttack(globalCoordsToPlayerSpace(attackerCoords),
                globalCoordsToPlayerSpace(defenderCoords));
    }

    public Coordinates globalCoordsToPlayerSpace(Coordinates coords) {
        Coordinates newCoords = new Coordinates();
        newCoords.setX(coords.getX());
        newCoords.setY(coords.getY());

        if (newCoords.getX() == 3) {
            newCoords.setX(0);
        } else if (newCoords.getX() == 2) {
            newCoords.setX(1);
        }
        return newCoords;
    }

    public int getPlayerAtTurnId() { return game.getPlayerAtTurnId(); }

    public Game getGame() { return game; }
    public int getCardsPerRow() { return cardsPerRow; }

}
