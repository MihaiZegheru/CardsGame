package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Ability;

import java.util.ArrayList;

public class CasterMinionData extends MinionData {

    protected final Ability ability;

    public CasterMinionData(int mana, int attackDamage, int health, Ability ability, String description,
                            ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
        this.ability = ability;
    }

    @JsonIgnore
    public Ability getAbility() { return ability; }
}
