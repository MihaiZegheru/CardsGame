package game.datacollections;

import java.util.ArrayList;

public final class PlayerData {
    private final ArrayList<DeckData> decks;
    private HeroData hero;
    private final String name;

    public PlayerData(final ArrayList<DeckData> decks, final String name) {
        this.decks = decks;
        this.name = name;
    }

    public ArrayList<DeckData> getDecks() {
        return decks;
    }

    public HeroData getHero() {
        return hero;
    }

    public String getName() {
        return name;
    }

    public void setHero(final HeroData hero) {
        this.hero = hero;
    }
}
