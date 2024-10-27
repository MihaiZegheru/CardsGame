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
    // Hosts the game entry for the game with a descriptor.
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
