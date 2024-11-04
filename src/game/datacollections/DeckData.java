package game.datacollections;

import java.util.ArrayList;

public final class DeckData {
    private final ArrayList<MinionData> cards;

    public DeckData(final ArrayList<MinionData> cards) {
        this.cards = cards;
    }

    public ArrayList<MinionData> getCards() {
        return cards;
    }
}
