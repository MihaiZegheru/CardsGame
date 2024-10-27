package game;

public class GameManager {
    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
}
