package game;

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

    public void StartGame(Player playerOne, Player playerTwo, int startingPlayer, int seed) {
        this.board = new Game(playerOne, playerTwo, seed);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Player GetPlayer(int id) {
        if (id == 1) {
            return  playerOne;
        } else if (id == 2) {
            return  playerTwo;
        } else {
            return null;
        }
    }
}
