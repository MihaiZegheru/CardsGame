package game.datacollections;

import java.util.ArrayList;

public class HeroData extends CardData {
    public HeroData(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, 30, description, colors, name);

    }

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
