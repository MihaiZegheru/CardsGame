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

    private int playerOneWins;
    private int playerTwoWins;
    private int gamesPlayed;

    ArrayList<GameObject> gameObjects = new ArrayList<>();

    private int roundCounter;
    private boolean turnSwitch;
    private int activePlayerId;

    private final int cardsPerRow = 5;

    private Game game;
    private Player playerOne;
    private Player playerTwo;

    boolean gameEnded;

    public void SetupTest() {
        playerOneWins = 0;
        playerTwoWins = 0;
        gamesPlayed = 0;
    }

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

        this.turnSwitch = true;
        this.roundCounter = 0;
        this.gameEnded = false;
        BeginPlay();
    }

    private void BeginPlay() {
        for (GameObject object : gameObjects) {
            object.BeginPlay();
        }
        roundCounter++;
    }

    private void TickRound() {
        for (GameObject object : gameObjects) {
            object.TickRound();
        }
        roundCounter++;
    }

    private void TickTurn() {
        for (GameObject object : gameObjects) {
            object.TickTurn();
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
        TickTurn();
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
        return getActivePlayer().UseMinionAttack(globalCoordsToPlayerSpace(attackerCoords),
                globalCoordsToPlayerSpace(defenderCoords));
    }

    public Status useMinionAbility(Coordinates casterCoords, Coordinates targetCoords) {
        if (activePlayerId == 1 && ((casterCoords.getX() != 2 && casterCoords.getX() != 3) ||
                casterCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((casterCoords.getX() != 0 && casterCoords.getX() != 1) ||
                casterCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        }
        return getActivePlayer().useMinionAbility(globalCoordsToPlayerSpace(casterCoords),
                globalCoordsToPlayerSpace(targetCoords));
    }

    public Status useMinionAttackHero(Coordinates attackerCoords) {
        if (activePlayerId == 1 && ((attackerCoords.getX() != 2 && attackerCoords.getX() != 3) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((attackerCoords.getX() != 0 && attackerCoords.getX() != 1) ||
                attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange, "Provided attacker coordinates exceed player's limit.");
        }
        Status status = getActivePlayer().attackHero(globalCoordsToPlayerSpace(attackerCoords));
        if (status.isOk() && gameEnded) {
            if (activePlayerId == 1) {
                playerOneWins++;
                gamesPlayed++;
                return new Status(StatusCode.kEnded, "Player one killed the enemy hero.");
            } else {
                playerTwoWins++;
                gamesPlayed++;
                return new Status(StatusCode.kEnded, "Player two killed the enemy hero.");
            }
        }
        return status;
    }

    public Status useHeroAbility(Coordinates targetCoords) {
        return getActivePlayer().useHeroAbility(globalCoordsToPlayerSpace(targetCoords));
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

    void onPlayerLost(Player player) {
        gameEnded = true;
    }

    public int getActivePlayerId() { return activePlayerId; }
    public Game getGame() { return game; }
    public int getCardsPerRow() { return cardsPerRow; }
    public int getPlayerOneWins() { return  playerOneWins; }
    public int getPlayerTwoWins() { return playerTwoWins; }
    public int getGamesPlayed() { return  gamesPlayed; }

}
