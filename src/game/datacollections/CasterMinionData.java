package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Ability;

import java.util.ArrayList;

public final class CasterMinionData extends MinionData {

    private final Ability ability;

    public CasterMinionData(final int mana, final int attackDamage, final int health,
                            final Ability ability, final String description,
                            final ArrayList<String> colors, final String name) {
        super(mana, attackDamage, health, description, colors, name);
        this.ability = ability;
    }

    @JsonIgnore
    public Ability getAbility() {
        return ability;
    }
}
