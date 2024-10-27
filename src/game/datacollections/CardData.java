package game.datacollections;

import java.util.ArrayList;

public class CardData {
    protected final int mana;
    protected final int health;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final String name;

    public CardData(int mana, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    public int getMana() { return mana; }
    public int getHealth() { return health; }
    public String getDescription() { return description; }
    public ArrayList<String> getColors() { return colors; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "CardData{"
                +  "mana="
                + mana
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                + name
                + '\''
                + '}';
    }
}
