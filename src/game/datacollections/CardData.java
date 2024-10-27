package game.datacollections;

import java.util.ArrayList;

public class CardData {
    public final int mana;
    public final int health;
    public final String description;
    public final ArrayList<String> colors;
    public final String Name;

    public CardData(int mana, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        Name = name;
    }
}
