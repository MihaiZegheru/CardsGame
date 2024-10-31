package game;

import fileio.Coordinates;
import game.datacollections.DeckData;
import game.datacollections.PlayerData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.System.exit;

public class GameManager {

    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    ArrayList<GameObject> gameObjects = new ArrayList<>();

    private int roundCounter = 0;
    private boolean turnSwitch = true;
    private int activePlayerId;

    private final int cardsPerRow = 5;

    private Game game;
    private Player playerOne;
    private Player playerTwo;

    public void Start(PlayerData playerOneData, int playerOneDeckIdx, PlayerData playerTwoData,
                      int playerTwoDeckIdx, int startingPlayerId, int seed) {
        DeckData playerOneDeck = playerOneData.getDecks().get(playerOneDeckIdx);
        this.playerOne = new Player(playerOneData, playerOneDeck, 1);

        DeckData playerTwoDeck = playerTwoData.getDecks().get(playerTwoDeckIdx);
        this.playerTwo = new Player(playerTwoData, playerTwoDeck, 2);

        Collections.shuffle(playerOne.getDeckCardsData(), new Random(seed));
        Collections.shuffle(playerTwo.getDeckCardsData(), new Random(seed));

        this.activePlayerId = startingPlayerId;
        this.game = new Game();
        TickRound();
    }

    private void TickRound() {
        for (GameObject object : gameObjects) {
            object.TickRound();
        }
        roundCounter++;
    }

    void RegisterGameObject(GameObject object) {
        if (gameObjects.contains(object)) {
            exit(-1);
        }
        gameObjects.add(object);
    }

    public void EndPlayerTurn() {
        turnSwitch = !turnSwitch;
        if (turnSwitch) {
            TickRound();
        }
        activePlayerId = activePlayerId == 1 ? 2 : 1;
    }

    Player getActivePlayer() {
        return  activePlayerId == 1 ? playerOne : playerTwo;
    }

    public StatusOr<Player> getPlayerById(int id) {
        if (id == 1) {
            return  new StatusOr<>(playerOne);
        } else if (id == 2) {
            return  new StatusOr<>(playerTwo);
        }
        return new StatusOr<>(StatusCode.kOutOfRange);
    }

    public Status PlaceCard(int idx) { return getActivePlayer().PlaceCard(idx); }

    public Status UseMinionAttack(Coordinates attackerCoords, Coordinates defenderCoords) {
        if (activePlayerId == 1 && ((attackerCoords.getX() != 2 && attackerCoords.getX() != 3) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((attackerCoords.getX() != 0 && attackerCoords.getX() != 1) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        }
//        if (activePlayerId == 1 && ((defenderCoords.getX() != 0 && defenderCoords.getX() != 1) ||
//                defenderCoords.getY() >= cardsPerRow)) {
//            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
//        } else if (activePlayerId == 2 && ((defenderCoords.getX() != 2 && defenderCoords.getX() != 3) ||
//                defenderCoords.getY() >= cardsPerRow)) {
//            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
//        }
        return getActivePlayer().UseMinionAttack(globalCoordsToPlayerSpace(attackerCoords),
                globalCoordsToPlayerSpace(defenderCoords));
    }

    public Status useMinionAbility(Coordinates attackerCoords, Coordinates defenderCoords) {
        if (activePlayerId == 1 && ((attackerCoords.getX() != 2 && attackerCoords.getX() != 3) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((attackerCoords.getX() != 0 && attackerCoords.getX() != 1) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        }
//        if (activePlayerId == 1 && ((defenderCoords.getX() != 0 && defenderCoords.getX() != 1) ||
//                defenderCoords.getY() >= cardsPerRow)) {
//            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
//        } else if (activePlayerId == 2 && ((defenderCoords.getX() != 2 && defenderCoords.getX() != 3) ||
//                defenderCoords.getY() >= cardsPerRow)) {
//            return new Status(StatusCode.kOutOfRange, "Provided defender coordinates exceed player's limit.");
//        }
        return getActivePlayer().UseMinionAttack(globalCoordsToPlayerSpace(attackerCoords),
                globalCoordsToPlayerSpace(defenderCoords));
    }

    public StatusOr<Minion> GetCardAt(Coordinates coords) {
        if (coords.getX() == 0 || coords.getX() == 1) {
            return playerTwo.getArmy().getMinionAt(globalCoordsToPlayerSpace(coords));
        } else if (coords.getX() == 2 || coords.getX() == 3) {
            return playerOne.getArmy().getMinionAt(globalCoordsToPlayerSpace(coords));
        }
        return new StatusOr<>(StatusCode.kOutOfRange, "Out of the board.");
    }

    public Coordinates globalCoordsToPlayerSpace(Coordinates coords) {
        Coordinates newCoords = new Coordinates();
        newCoords.setX(coords.getX());
        newCoords.setY(coords.getY());
        if (activePlayerId == 1 && (newCoords.getX() == 0 || newCoords.getX() == 1)) {
            newCoords.setIsEnemyPosition(true);
        } else if (activePlayerId == 2 && (newCoords.getX() == 2 || newCoords.getX() == 3)) {
            newCoords.setIsEnemyPosition(true);
        }

        if (newCoords.getX() == 3) {
            newCoords.setX(0);
        } else if (newCoords.getX() == 2) {
            newCoords.setX(1);
        }
        return newCoords;
    }

    Player getOtherPlayer(Player self) {
        return self == playerOne ? playerTwo : playerOne;
    }

    public int getActivePlayerId() { return activePlayerId; }
    public Game getGame() { return game; }
    public int getCardsPerRow() { return cardsPerRow; }

}
