package game;

public class GameManager {
    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    Player playerOne;
    Player playerTwo;

    public void StartGame(Player playerOne, Player playerTwo) {

    }
}
