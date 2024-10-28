package game;

import game.datacollections.*;
import utility.Status;

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

    private int ddRow;
    private int tankRow;

    private int currMana;

    public Player(PlayerData data, DeckData deckData, int ddRow, int tankRow, int id) {
        this.data = data;
        this.deckCards = new LinkedList<>();
        this.deckCards.addAll(deckData.getCards());
        this.handCards = new ArrayList<>();
        this.boardCards = new ArrayList<>();
        this.hero = new Hero(data.getHero());
        this.id = id;
        this.ddRow = ddRow;
        this.tankRow = tankRow;
    }

    public void DrawCard() {
        handCards.add(deckCards.poll());
    }

    public void IncreaseCurrMana(int inc) {
        currMana += inc;
    }

    public Status PlaceCard(int idx) {
        if (idx >= handCards.size()) {
            return Status.outOfRange();
        }
        if (handCards.get(idx).getMana() > currMana) {
            return Status.notEnoughManaToPlace();
        }
        Minion placedMinion = GameManager.GetInstance().getGame().PlaceCard(this, handCards.get(idx));
        if (placedMinion == null) {
            return Status.aborted();
        }
        currMana -= handCards.get(idx).getMana();
        boardCards.add(placedMinion);
        handCards.remove(idx);
        return Status.ok();
    }

    public void EndTurn() {
        GameManager.GetInstance().getGame().OnEndPlayerTurn(this);
    }

    public List<?> getDeckCardsData() { return (List<?>) deckCards; }
    public ArrayList<MinionData> getCardsInHand() { return handCards; }
    public HeroData getHeroData() { return data.getHero(); }
    public int getCurrMana() { return currMana; }
    public int getId() { return id; }
    public int getDdRow() {return ddRow; }
    public int getTankRow() { return tankRow; }

    @Override
    public String toString() {
        return "Player{" +
                "handCards=" + deckCards +
                ", boardCards=" + boardCards +
                ", hero=" + hero +
                '}';
    }
}
