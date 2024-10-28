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

    private Game board;
    private Player playerOne;
    private Player playerTwo;

    public void StartGame(PlayerData playerOneData, int playerOneDeckIdx, PlayerData playerTwoData,
                          int playerTwoDeckIdx, int startingPlayerId, int seed) {
        DeckData playerOneDeck = playerOneData.getDecks().get(playerOneDeckIdx);
        this.playerOne = new Player(playerOneData, playerOneDeck, 1);

        DeckData playerTwoDeck = playerTwoData.getDecks().get(playerTwoDeckIdx);
        this.playerTwo = new Player(playerTwoData, playerTwoDeck, 2);

        this.board = new Game(playerOne, playerTwo, startingPlayerId, seed);
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

    public void EndPlayerTurn() { getPlayer(board.getPlayerAtTurnId()).EndTurn(); }

    public void PlaceCard(int idx) { getPlayer(board.getPlayerAtTurnId()).PlaceCard(idx); }

    public int getPlayerAtTurnId() { return board.getPlayerAtTurnId(); }

    public Game getBoard() { return board; }
}
