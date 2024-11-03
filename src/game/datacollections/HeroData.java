package game.datacollections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Ability;

import java.util.ArrayList;

public class HeroData extends CardData {

    protected final Ability ability;

    public HeroData(int mana,  Ability ability, String description, ArrayList<String> colors, String name) {
        super(mana, 30, description, colors, name);
        this.ability = ability;
    }

    @JsonIgnore
    public Ability getAbility() { return ability; }

    @Override
    public String toString() {
        return "HeroData{"
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
