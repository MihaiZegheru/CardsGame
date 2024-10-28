package game;

import game.datacollections.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Player {
    private PlayerData data;
    private int id;

    private Queue<MinionData> deckCards;
    private ArrayList<MinionData> handCards;
    private ArrayList<Minion> boardCards;
    private Hero hero;

    private int currMana;

    public Player(PlayerData data, DeckData deckData, int id) {
        this.data = data;
        this.deckCards = new LinkedList<>();
        this.deckCards.addAll(deckData.getCards());
        this.handCards = new ArrayList<>();
        this.boardCards = new ArrayList<>();
        this.hero = new Hero(data.getHero());
        this.id = id;
    }

    public void DrawCard() {
        handCards.add(deckCards.poll());
    }

    public void IncreaseCurrMana(int inc) {
        currMana += inc;
    }

    public void PlaceCard(int idx) {
        if (idx >= handCards.size()) {
            System.out.println("Card idx out of bounds.");
            return;
        }
        if (handCards.get(idx).getMana() > currMana) {
            System.out.println("Not enough mana to cast that.");
            return;
        }
        Minion placedMinion = GameManager.GetInstance().getBoard().PlaceCard(this, handCards.get(idx));
        if (placedMinion == null) {
            System.out.println("Not player's turn.");
            return;
        }
        currMana -= handCards.get(idx).getMana();
        boardCards.add(placedMinion);
        handCards.remove(idx);
    }

    public void EndTurn() {
        GameManager.GetInstance().getBoard().OnEndPlayerTurn(this);
    }

    public List<?> getDeckCardsData() { return (List<?>) deckCards; }
    public ArrayList<MinionData> getCardsInHand() { return handCards; }
    public HeroData getHeroData() { return data.getHero(); }
    public int getCurrMana() { return currMana; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return "Player{" +
                "handCards=" + deckCards +
                ", boardCards=" + boardCards +
                ", hero=" + hero +
                '}';
    }
}
