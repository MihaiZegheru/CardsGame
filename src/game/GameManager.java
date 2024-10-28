package game;

import game.datacollections.DeckData;
import game.datacollections.PlayerData;

public class GameManager {
    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private Game game;
    private Player playerOne;
    private Player playerTwo;

    public void StartGame(PlayerData playerOneData, int playerOneDeckIdx, PlayerData playerTwoData,
                          int playerTwoDeckIdx, int startingPlayerId, int seed) {
        DeckData playerOneDeck = playerOneData.getDecks().get(playerOneDeckIdx);
        this.playerOne = new Player(playerOneData, playerOneDeck, 3, 2, 1);

        DeckData playerTwoDeck = playerTwoData.getDecks().get(playerTwoDeckIdx);
        this.playerTwo = new Player(playerTwoData, playerTwoDeck, 0,1, 2);

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

    public void PlaceCard(int idx) { getPlayer(game.getPlayerAtTurnId()).PlaceCard(idx); }

    public int getPlayerAtTurnId() { return game.getPlayerAtTurnId(); }

    public Game getGame() { return game; }
}
