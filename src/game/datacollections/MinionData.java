package game.datacollections;

import java.util.ArrayList;

public class MinionData extends CardData {
    protected final int attackDamage;

    public MinionData(int mana, int attackDamage, int health, String description, ArrayList<String> colors,
                      String name) {
        super(mana, health, description, colors, name);
        this.attackDamage = attackDamage;
    }

    public int getAttackDamage() { return attackDamage; }

    @Override
    public String toString() {
        return "{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackDamage
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
