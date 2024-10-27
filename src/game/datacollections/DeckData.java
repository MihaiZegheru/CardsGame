package game.datacollections;

import java.util.ArrayList;

public class DeckData {
    protected final ArrayList<CardData> cards;

    public DeckData(ArrayList<CardData> cards) {
        this.cards = cards;
    }

    public ArrayList<CardData> getCards() { return cards; }

    @Override
    public String toString() {
        return "InfoInput{"
                + ", cards="
                + cards
                + '}';
    }
}
