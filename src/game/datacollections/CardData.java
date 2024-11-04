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

    public CardData(final int mana, final int health, final String description,
                    final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.type = WarriorType.resolveWarriorType(this);
    }

    public final int getMana() {
        return mana;
    }

    public final int getHealth() {
        return health;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    @JsonIgnore
    public final WarriorType getType() {
        return type;
    }
}
