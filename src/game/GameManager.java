package game;

import fileio.Input;

public class GameManager {
    private static GameManager instance = null;
    public static GameManager GetInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private Input descriptor;

    // TODO: Return a game-end object.
    // Hosts the main loop of the game with a game descriptor.
    public void Start(Input descriptor) {
        if (descriptor == null) {
            System.out.println("Descriptor not loaded.");
            System.exit(-1);
        }
        this.descriptor = descriptor;

        // Game lop goes here

        // Set null after every game.
        descriptor = null;
    }
}
