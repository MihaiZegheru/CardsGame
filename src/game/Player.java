package game;

import fileio.Coordinates;
import game.datacollections.*;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static utility.Math.Clamp;


public class Player extends GameObject {
    private PlayerData data;
    private int id;

    private Queue<MinionData> deckCards;
    private ArrayList<MinionData> handCards;
    private ArrayList<Minion> boardCards;
    private Hero hero;

    private Army army;

    private int manaFlow = 0;
    private int currMana = 0;

    public Player(PlayerData data, DeckData deckData, int id) {
        this.data = data;
        this.deckCards = new LinkedList<>();
        this.deckCards.addAll(deckData.getCards());
        this.handCards = new ArrayList<>();
        this.boardCards = new ArrayList<>();
//        this.hero = new Hero(data.getHero(), army);
        this.id = id;
        this.army = new Army(new Hero(data.getHero(), army));
        army.setParent(this);
    }

    @Override
    void BeginPlay() {}

    @Override
    void TickRound() {
        DrawCard();
        HandleMana();
    }

    public void DrawCard() {
        handCards.add(deckCards.poll());
    }

    public void HandleMana() {
        manaFlow = Clamp(manaFlow + 1, 0, 10);
        currMana += manaFlow;
    }

    public Status PlaceCard(int idx) {
        if (idx >= handCards.size()) {
            return new Status(StatusCode.kOutOfRange, "Index out of range.");
        }
        if (handCards.get(idx).getMana() > currMana) {
            return new Status(StatusCode.kAborted, "Not enough mana to place card on table.");
        }

        Status placeStatus = army.PlaceMinion(handCards.get(idx));
        if (!placeStatus.isOk()) {
            return placeStatus;
        }
        currMana -= handCards.get(idx).getMana();
        handCards.remove(idx);
        return Status.ok();
    }

    public Status UseMinionAttack(Coordinates attackerCoords, Coordinates defenderCoords) {
        System.out.println("AAAAAAAAA");
        // TODO: Attack is different from ability. Cannot reuse logic. This is what causes bug.
        // Attack doesn't care about supports.
        System.out.println(defenderCoords.getIsEnemyPosition());
        StatusOr<Minion> minion = army.getMinionAt(attackerCoords);
        if (!minion.isOk()) {
            return minion;
        }
        return GameManager.GetInstance().getGame().AttackAt(this, minion.unwrap(), defenderCoords);
    }

    public List<?> getDeckCardsData() { return (List<?>) deckCards; }
    public ArrayList<MinionData> getCardsInHand() { return handCards; }
    public HeroData getHeroData() { return data.getHero(); }
    public int getCurrMana() { return currMana; }
    public int getId() { return id; }
    public Army getArmy() { return army; }

    @Override
    public String toString() {
        return "Player{" +
                "handCards=" + deckCards +
                ", boardCards=" + boardCards +
                ", hero=" + hero +
                '}';
    }
}
