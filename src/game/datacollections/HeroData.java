package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Ability;
import game.GameManager;

import java.util.ArrayList;

public final class HeroData extends CardData {

    private final Ability ability;

    public HeroData(final int mana, final Ability ability, final String description,
                    final ArrayList<String> colors, final String name) {
        super(mana, GameManager.getInstance().getHeroMaxHP(), description, colors, name);
        this.ability = ability;
    }

    @JsonIgnore
    public Ability getAbility() {
        return ability;
    }
}
