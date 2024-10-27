package game;

import game.datacollections.*;

import java.util.ArrayList;

public class Player {
    private ArrayList<MinionData> handCards;
    private ArrayList<Minion> boardCards;
    private Hero hero;

    public Player(DeckData deckData, HeroData heroData) {
        this.handCards = deckData.getCards();
        this.hero = new Hero(heroData);
    }

    @Override
    public String toString() {
        return "Player{" +
                "handCards=" + handCards +
                ", boardCards=" + boardCards +
                ", hero=" + hero +
                '}';
    }
}
