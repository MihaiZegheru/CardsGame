package game;

import fileio.Coordinates;
import game.datacollections.DeckData;
import game.datacollections.MinionData;
import game.datacollections.PlayerData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static utility.Math.clamp;


public final class Player extends GameObject {

    private Queue<MinionData> deckCards;
    private ArrayList<MinionData> handCards;

    private Army army;

    private int manaFlow = 0;
    private int currMana = 0;

    public Player(final PlayerData data, final DeckData deckData) {
        this.deckCards = new LinkedList<>();
        this.deckCards.addAll(deckData.getCards());
        this.handCards = new ArrayList<>();
        this.army = new Army();
        this.army.setParent(this);
        this.army.setHero(new Hero(data.getHero(), army));
    }

    @Override
    void beginPlay() {
        drawCard();
        handleMana();
    }

    @Override
    void tickRound() {
        drawCard();
        handleMana();
    }

    @Override
    void tickTurn() {

    }

    /**
     * Get a card from deckCards and add it to handCard.
     *
     * @implNote Checks if deckCards is empty.
     */
    public void drawCard() {
        if (deckCards.isEmpty()) {
            return;
        }
        handCards.add(deckCards.poll());
    }

    /**
     * Updates mana. Ticked on every round.
     */
    public void handleMana() {
        manaFlow = clamp(manaFlow + 1, 0, GameManager.getInstance().getManaMaxRate());
        currMana += manaFlow;
    }

    /**
     * Tries placing card at idx from hand.
     *
     * @param idx
     * @return Status
     */
    public Status placeCard(final int idx) {
        if (idx >= handCards.size()) {
            return new Status(StatusCode.kOutOfRange, "Index out of range.");
        }
        if (handCards.get(idx).getMana() > currMana) {
            return new Status(StatusCode.kAborted, "Not enough mana to place card on table.");
        }
        Status placeStatus = army.placeMinion(handCards.get(idx));
        if (!placeStatus.isOk()) {
            return placeStatus;
        }
        currMana -= handCards.get(idx).getMana();
        handCards.remove(idx);
        return Status.ok();
    }

    /**
     * Tries attacking from attackerCoords at defenderCoords.
     *
     * @param attackerCoords
     * @param defenderCoords
     * @return Status
     */
    public Status useMinionAttack(final Coordinates attackerCoords,
                                  final Coordinates defenderCoords) {
        StatusOr<Minion> minion = army.getMinionAt(attackerCoords);
        if (!minion.isOk()) {
            return minion;
        }
        return GameManager.getInstance().getGame().attackAt(this, minion.unwrap(),
                defenderCoords);
    }

    /**
     * Tries casting.using an ability from casterCoords at targetCoords.
     *
     * @param casterCoords
     * @param targetCoords
     * @return
     */
    public Status useMinionAbility(final Coordinates casterCoords, final Coordinates targetCoords) {
        StatusOr<Minion> minionStatus = army.getMinionAt(casterCoords);
        if (!minionStatus.isOk()) {
            return minionStatus;
        }
        Minion minion = minionStatus.unwrap();
        if (!minion.getData().getType().isAny(WarriorType.getDruid()
                | WarriorType.getShadow())) {
            return new StatusOr<>(StatusCode.kAborted, "Selected minion is not a caster.");
        }
        return GameManager.getInstance().getGame().castAt(this, minion, targetCoords);
    }

    /**
     * Tries using hero ability at targetCoords.
     *
     * @param targetCoords Should represent a row i.e. y doesn't matter.
     * @return Status
     */
    public Status useHeroAbility(final Coordinates targetCoords) {
        Hero hero = army.getHero();
        if (hero.getMana() > currMana) {
            return new Status(StatusCode.kAborted, "Not enough mana to use hero's ability.");
        }
        Status heroCastStatus = GameManager.getInstance().getGame().heroCastAt(this, hero,
                targetCoords);
        if (!heroCastStatus.isOk()) {
            return heroCastStatus;
        }
        currMana -= hero.getMana();
        return Status.ok();
    }

    /**
     * Tries attacking enemy hero.
     *
     * @param attackerCoords
     * @return Status
     */
    public Status attackHero(final Coordinates attackerCoords) {
        StatusOr<Minion> minionStatus = army.getMinionAt(attackerCoords);
        if (!minionStatus.isOk()) {
            return minionStatus;
        }
        return GameManager.getInstance().getGame().attackHero(this, minionStatus.unwrap());
    }

    public List<?> getDeckCardsData() {
        return (List<?>) deckCards;
    }

    public ArrayList<MinionData> getCardsInHand() {
        return handCards;
    }

    public int getCurrMana() {
        return currMana;
    }

    public Army getArmy() {
        return army;
    }
}
