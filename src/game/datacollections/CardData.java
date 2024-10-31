package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.WarriorType;

import java.util.ArrayList;

public class CardData {
    protected final int mana;
    protected final int health;
    protected final String description;
    protected final ArrayList<String> colors;
    protected final String name;
    protected final WarriorType type;

    public CardData(int mana, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.type = WarriorType.ResolveWarriorType(this);
    }

    public int getMana() { return mana; }
    public int getHealth() { return health; }
    public String getDescription() { return description; }
    public ArrayList<String> getColors() { return colors; }
    public String getName() { return name; }
    @JsonIgnore
    public WarriorType getType() { return type; }

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
