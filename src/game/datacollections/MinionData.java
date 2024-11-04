package game.datacollections;

import java.util.ArrayList;

public class MinionData extends CardData {
    protected final int attackDamage;

    public MinionData(final int mana, final int attackDamage, final int health,
                      final String description, final ArrayList<String> colors, final String name) {
        super(mana, health, description, colors, name);
        this.attackDamage = attackDamage;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }
}
