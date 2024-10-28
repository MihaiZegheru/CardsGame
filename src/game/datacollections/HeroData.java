package game.datacollections;

import java.util.ArrayList;

public class HeroData extends CardData {
    public HeroData(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, 30, description, colors, name);
    }
}
