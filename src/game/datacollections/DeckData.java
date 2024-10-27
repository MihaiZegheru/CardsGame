package game.datacollections;

import java.util.ArrayList;

public class DeckData {
    protected final ArrayList<MinionData> cards;

    public DeckData(ArrayList<MinionData> cards) {
        this.cards = cards;
    }

    public ArrayList<MinionData> getCards() { return cards; }

    @Override
    public String toString() {
        return "DeckData{"
                + ", cards="
                + cards
                + '}';
    }
}
