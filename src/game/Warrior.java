package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.datacollections.CardData;

import java.util.ArrayList;

import static utility.Math.clamp;

public abstract class Warrior extends GameObject {
    protected final CardData data;
    protected int health;
    protected boolean hasAttacked;
    protected boolean isFrozen;
    protected int frozenTimer;
    protected Army army;

    public Warrior(final CardData data, final Army army) {
        super();
        this.data = data;
        this.health = data.getHealth();
        this.hasAttacked = false;
        this.isFrozen = false;
        this.army = army;
        this.frozenTimer = 0;
    }

    @Override
    void beginPlay() {

    }

    /**
     * Call super() if overridden.
     */
    @Override
    void tickRound() {
        hasAttacked = false;
    }

    /**
     * Call super() if overridden.
     */
    @Override
    void tickTurn() {
        if (isFrozen) {
            frozenTimer--;
        }
        if (frozenTimer == 0) {
            isFrozen = false;
        }
    }

    /**
     * Event triggered by this being attacked.
     *
     * @implNote This receives damage. If health <= 0, trigger onDied event.
     *
     * @param damage
     */
    public final void onAttacked(final int damage) {
        health = clamp(health - damage, 0, Integer.MAX_VALUE);
        if (health <= 0) {
            onDied();
        }
    }

    /**
     * Should be overridden by implementing death logic.
     */
    protected void onDied() {

    }

    public final int getMana() {
        return data.getMana();
    }

    public final int getHealth() {
        return health;
    }

    public final String getDescription() {
        return data.getDescription();
    }

    public final ArrayList<String> getColors() {
        return data.getColors();
    }

    public final String getName() {
        return data.getName();
    }

    @JsonIgnore
    public final boolean getHasAttacked() {
        return hasAttacked;
    }

    @JsonIgnore
    public final boolean getIsFrozen() {
        return isFrozen;
    }

    @JsonIgnore
    public final CardData getData() {
        return data;
    }

    @JsonIgnore
    public final WarriorType getType() {
        return data.getType();
    }
}
