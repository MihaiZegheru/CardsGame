package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.datacollections.CardData;
import status.Status;

import java.util.ArrayList;

import static utility.Math.Clamp;

public abstract class Warrior extends GameObject {
    protected CardData data;
    protected int health;
    protected boolean hasAttacked;
    protected boolean isFrozen;
    protected Army army;

    public Warrior(CardData data, Army army) {
        super();
        this.data = data;
        this.health = data.getHealth();
        this.hasAttacked = false;
        this.isFrozen = false;
        this.army = army;
    }

    @Override
    void BeginPlay() {}

    @Override
    void TickRound() {
        hasAttacked = false;
        isFrozen = false;
    }

    public void OnAttacked(int damage) {
        System.out.println(health);
        health = Clamp(health - damage, 0, data.getHealth());
        System.out.println(health);
        System.out.println(damage);
        if (health <= 0) {
            OnDied();
        }
    }

    protected void OnDied() {}

    public int getMana() { return data.getMana(); }
    public int getHealth() { return health; }
    public String getDescription() { return data.getDescription(); }
    public ArrayList<String> getColors() { return data.getColors(); }
    public String getName() { return data.getName(); }
    @JsonIgnore
    public boolean getHasAttacked() { return hasAttacked; }
    @JsonIgnore
    public boolean getIsFrozen() { return isFrozen; }
    @JsonIgnore
    public CardData getData() { return data; }
    @JsonIgnore
    public WarriorType getType() { return data.getType(); }

    public void setHasAttacked(boolean hasAttacked) { this.hasAttacked = hasAttacked; }
    public void setIsFrozen(boolean isFrozen) { this.isFrozen = isFrozen; }
}
