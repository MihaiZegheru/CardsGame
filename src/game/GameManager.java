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

/**
 * Singleton that manages the game.
 */
public final class GameManager {

    private static GameManager instance = null;

    /**
     *
     * @return instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private int playerOneWins;
    private int playerTwoWins;
    private int gamesPlayed;

    private ArrayList<GameObject> gameObjects;

    private int roundCounter;
    private boolean turnSwitch;
    private int activePlayerId;

    private final int cardsPerRow = 5;
    private final int heroMaxHP = 30;
    private final int manaMaxRate = 10;
    private final int playerTwoBackRowIdx = 0;
    private final int playerTwoFrontRowIdx = 1;
    private final int playerOneFrontRowIdx = 2;
    private final int playerOneBackRowIdx = 3;

    private Game game;
    private Player playerOne;
    private Player playerTwo;

    private boolean gameEnded;

    /**
     * Sets up initial values.
     */
    public void setupTest() {
        playerOneWins = 0;
        playerTwoWins = 0;
        gamesPlayed = 0;
    }

    /**
     * Builds and starts a new game.
     *
     * @param playerOneData
     * @param playerOneDeckIdx
     * @param playerTwoData
     * @param playerTwoDeckIdx
     * @param startingPlayerId
     * @param seed
     */
    public void start(final PlayerData playerOneData, final int playerOneDeckIdx,
                      final PlayerData playerTwoData, final int playerTwoDeckIdx,
                      final int startingPlayerId, final int seed) {
        this.gameObjects = new ArrayList<>();
        DeckData playerOneDeck = playerOneData.getDecks().get(playerOneDeckIdx);
        this.playerOne = new Player(playerOneData, playerOneDeck);

        DeckData playerTwoDeck = playerTwoData.getDecks().get(playerTwoDeckIdx);
        this.playerTwo = new Player(playerTwoData, playerTwoDeck);

        Collections.shuffle(playerOne.getDeckCardsData(), new Random(seed));
        Collections.shuffle(playerTwo.getDeckCardsData(), new Random(seed));

        this.activePlayerId = startingPlayerId;
        this.game = new Game();

        this.turnSwitch = true;
        this.roundCounter = 0;
        this.gameEnded = false;
        beginPlay();
    }

    /**
     * calls beginPlay on all GameObjects.
     */
    private void beginPlay() {
        for (GameObject object : gameObjects) {
            object.beginPlay();
        }
        roundCounter++;
    }

    /**
     * calls tickRound on all GameObjects. Used for round changes.
     */
    private void tickRound() {
        for (GameObject object : gameObjects) {
            object.tickRound();
        }
        roundCounter++;
    }

    /**
     * calls tickTurn on all GameObjects. Used for turn changes.
     */
    private void tickTurn() {
        for (GameObject object : gameObjects) {
            object.tickTurn();
        }
        roundCounter++;
    }

    void registerGameObject(final GameObject object) {
        if (gameObjects.contains(object)) {
            exit(-1);
        }
        gameObjects.add(object);
    }

    /**
     * Entry point for endPlayerTurn command.
     */
    public void endPlayerTurn() {
        turnSwitch = !turnSwitch;
        tickTurn();
        if (turnSwitch) {
            tickRound();
        }
        activePlayerId = activePlayerId == 1 ? 2 : 1;
    }

    /**
     * Entry point for placeCard command.
     *
     * @param idx
     * @return Status
     */
    public Status placeCard(final int idx) {
        return getActivePlayer().placeCard(idx);
    }

    /**
     * Entry point for useMinionAttack command.
     *
     * @param attackerCoords
     * @param defenderCoords
     * @return Status
     */
    public Status useMinionAttack(final Coordinates attackerCoords,
                                  final Coordinates defenderCoords) {
        if (activePlayerId == 1 && ((attackerCoords.getX() != playerOneFrontRowIdx
                && attackerCoords.getX() != playerOneBackRowIdx)
                || attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((attackerCoords.getX() != playerTwoBackRowIdx
                && attackerCoords.getX() != playerTwoFrontRowIdx)
                || attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
        }
        return getActivePlayer().useMinionAttack(globalCoordsToPlayerSpace(attackerCoords),
                globalCoordsToPlayerSpace(defenderCoords));
    }

    /**
     * Entry point for useMinionAbility command.
     *
     * @param casterCoords
     * @param targetCoords
     * @return Status
     */
    public Status useMinionAbility(final Coordinates casterCoords,
                                   final Coordinates targetCoords) {
        if (activePlayerId == 1 && ((casterCoords.getX() != playerOneFrontRowIdx
                && casterCoords.getX() != playerOneBackRowIdx)
                || casterCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((casterCoords.getX() != playerTwoBackRowIdx
                && casterCoords.getX() != playerTwoFrontRowIdx)
                || casterCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
        }
        return getActivePlayer().useMinionAbility(globalCoordsToPlayerSpace(casterCoords),
                globalCoordsToPlayerSpace(targetCoords));
    }

    /**
     * Entry point for useMinionAttackHero command.
     *
     * @param attackerCoords
     * @return Status
     */
    public Status useMinionAttackHero(final Coordinates attackerCoords) {
        if (activePlayerId == 1 && ((attackerCoords.getX() != playerOneFrontRowIdx
                && attackerCoords.getX() != playerOneBackRowIdx)
                || attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
        } else if (activePlayerId == 2 && ((attackerCoords.getX() != playerTwoBackRowIdx
                && attackerCoords.getX() != playerTwoFrontRowIdx)
                || attackerCoords.getY() >= cardsPerRow)) {
            return new Status(StatusCode.kOutOfRange,
                    "Provided attacker coordinates exceed player's limit.");
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

    /**
     * Entry point for useHeroAbility command.
     *
     * @param targetCoords
     * @return Status
     */
    public Status useHeroAbility(final Coordinates targetCoords) {
        return getActivePlayer().useHeroAbility(globalCoordsToPlayerSpace(targetCoords));
    }

    StatusOr<Minion> getCardAt(final Coordinates coords) {
        if (coords.getX() == playerTwoBackRowIdx || coords.getX() == playerTwoFrontRowIdx) {
            return playerTwo.getArmy().getMinionAt(globalCoordsToPlayerSpace(coords));
        } else if (coords.getX() == playerOneFrontRowIdx || coords.getX() == playerOneBackRowIdx) {
            return playerOne.getArmy().getMinionAt(globalCoordsToPlayerSpace(coords));
        }
        return new StatusOr<>(StatusCode.kOutOfRange, "Out of the board.");
    }

    /**
     * Converts global coords int local player space. BackLine => 0, FrontLine => 1.
     *
     * @param coords
     * @return Coordinates
     */
    public Coordinates globalCoordsToPlayerSpace(final Coordinates coords) {
        Coordinates newCoords = new Coordinates();
        newCoords.setX(coords.getX());
        newCoords.setY(coords.getY());
        if (activePlayerId == 1 && (newCoords.getX() == playerTwoBackRowIdx
                || newCoords.getX() == playerTwoFrontRowIdx)) {
            newCoords.setIsEnemyPosition(true);
        } else if (activePlayerId == 2 && (newCoords.getX() == playerOneFrontRowIdx
                || newCoords.getX() == playerOneBackRowIdx)) {
            newCoords.setIsEnemyPosition(true);
        }

        if (newCoords.getX() == playerOneBackRowIdx) {
            newCoords.setX(0);
        } else if (newCoords.getX() == playerOneFrontRowIdx) {
            newCoords.setX(1);
        }
        return newCoords;
    }

    StatusOr<Player> getPlayerById(final int id) {
        if (id == 1) {
            return new StatusOr<>(playerOne);
        } else if (id == 2) {
            return new StatusOr<>(playerTwo);
        }
        return new StatusOr<>(StatusCode.kOutOfRange);
    }

    Player getActivePlayer() {
        return activePlayerId == 1 ? playerOne : playerTwo;
    }

    Player getOtherPlayer(final Player player) {
        return player == playerOne ? playerTwo : playerOne;
    }

    void onPlayerLost(final Player player) {
        gameEnded = true;
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public Game getGame() {
        return game;
    }

    public int getCardsPerRow() {
        return cardsPerRow;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getHeroMaxHP() {
        return heroMaxHP;
    }

    public int getManaMaxRate() {
        return manaMaxRate;
    }
}
