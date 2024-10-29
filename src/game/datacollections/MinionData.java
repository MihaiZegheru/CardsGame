package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import game.MinionType;

import java.util.ArrayList;

public class MinionData extends CardData {
    protected final int attackDamage;
    protected MinionType type;

    public MinionData(int mana, int attackDamage, int health, String description, ArrayList<String> colors,
                      String name) {
        super(mana, health, description, colors, name);
        this.attackDamage = attackDamage;
        this.type = MinionType.ResolveMinionType(this);
    }

    public int getAttackDamage() { return attackDamage; }
    @JsonIgnore
    public MinionType getType() { return type; }

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
