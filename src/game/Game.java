package game;


import java.util.Collections;
import java.util.Random;

public class Game {
    private Player playerOne;
    private Player playerTwo;
    private int seed;

    public Game(Player playerOne, Player playerTwo, int seed) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.seed = seed;

        Collections.shuffle(playerOne.getDeckCardsData(), new Random(seed));
        Collections.shuffle(playerTwo.getDeckCardsData(), new Random(seed));
    }
}
