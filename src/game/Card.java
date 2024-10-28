package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.datacollections.CardData;

import java.util.ArrayList;

import static utility.Math.Clamp;

public class Card {
    protected CardData data;
    protected int health;
    protected boolean hasAttacked;
    protected boolean isFrozen;

    public Card(CardData data) {
        this.data = data;
        this.health = data.getHealth();
        this.hasAttacked = false;
        this.isFrozen = false;
    }

    public void OnAttacked(int damage) {
        health = Clamp(health - damage, 0, data.getHealth());
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

    public void setHasAttacked(boolean hasAttacked) { this.hasAttacked = hasAttacked; }
    public void setIsFrozen(boolean isFrozen) { this.isFrozen = isFrozen; }

}
