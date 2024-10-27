package game.datacollections;

import java.util.ArrayList;

public class PlayerData {
    private final ArrayList<DeckData> decks;
    private HeroData hero;
    private final String name;

    public PlayerData(ArrayList<DeckData> decks, String name) {
        this.decks = decks;
        this.name = name;
    }

    public ArrayList<DeckData> getDecks() { return decks; }
    public HeroData getHero() {  return hero; }
    public String getName() { return name; }

    public void setHero(HeroData hero) { this.hero = hero; }

    @Override
    public String toString() {
        return "PlayerData{"
                + "name="
                + name
                + ", Hero="
                + hero
                + ", decks="
                + decks
                + '}';
    }
}
