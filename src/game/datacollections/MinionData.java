package game.datacollections;

import java.util.ArrayList;

public class MinionData extends CardData {
    public final int attackDamage;

    public MinionData(int mana, int health, String description, ArrayList<String> colors, String name,
                      int attackDamage) {
        super(mana, health, description, colors, name);
        this.attackDamage = attackDamage;
    }
}
