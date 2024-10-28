package game;

import game.datacollections.*;

import java.util.ArrayList;


public class Player {
    private PlayerData data;

    private ArrayList<MinionData> deckCards;
    private ArrayList<MinionData> handCards;
    private ArrayList<Minion> boardCards;
    private Hero hero;

    public Player(PlayerData data, DeckData deckData) {
        this.data = data;
        this.deckCards = new ArrayList<>();
        this.deckCards.addAll(deckData.getCards());
        this.handCards = new ArrayList<>();
        this.boardCards = new ArrayList<>();
        this.hero = new Hero(data.getHero());
    }



    public ArrayList<MinionData> getDeckCardsData() { return deckCards; }
    public HeroData getHeroData() { return data.getHero(); }

    @Override
    public String toString() {
        return "Player{" +
                "handCards=" + deckCards +
                ", boardCards=" + boardCards +
                ", hero=" + hero +
                '}';
    }
}
