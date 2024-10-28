package game;

import game.datacollections.CardData;
import game.datacollections.MinionData;

import java.util.ArrayList;

public class Card {
    protected CardData data;
    protected int health;

    public Card(CardData data) {
        this.data = data;
        this.health = data.getHealth();
    }

    public int getMana() { return data.getMana(); }
    public int getHealth() { return health; }
    public String getDescription() { return data.getDescription(); }
    public ArrayList<String> getColors() { return data.getColors(); }
    public String getName() { return data.getName(); }

}
