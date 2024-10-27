package game;

import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;
import game.datacollections.CardData;
import game.datacollections.DeckData;
import game.datacollections.PlayerData;

import java.util.ArrayList;

public class Mocker {
    // TODO: Return a game-end object.
    // Mock a game for the given input. This function sets up all the game variables so that it keeps the game logic
    // separate, calling its API.
    public void Mock(Input descriptor) {
        if (descriptor == null) {
            System.out.println("Input Descriptor not loaded.");
            System.exit(-1);
        }

        Player playerOne = BuildPlayer(descriptor.getPlayerOneDecks(), "playerOne");
        Player playerTwo = BuildPlayer(descriptor.getPlayerTwoDecks(), "playerTwo");

        // Set null after every mock.
        descriptor = null;
    }

    private ArrayList<DeckData> BuildDecksDataFromInputObject(DecksInput input) {
        ArrayList<DeckData> decksData = new ArrayList<>(input.getNrDecks());
        for (int i = 0; i < input.getNrDecks(); ++i) {
            ArrayList<CardData> cardsData = new ArrayList<>(input.getNrCardsInDeck());
            for (int j = 0; j < input.getNrCardsInDeck(); ++j) {
                CardInput currCardInput = input.getDecks().get(i).get(j);
                cardsData.add(new CardData(currCardInput.getMana(), currCardInput.getHealth(),
                        currCardInput.getDescription(), currCardInput.getColors(), currCardInput.getName()));
            }
            decksData.add(new DeckData(cardsData));
        }
        return decksData;
    }

    private Player BuildPlayer(DecksInput decksDescriptor, String name) {
        PlayerData playerData = new PlayerData(BuildDecksDataFromInputObject(decksDescriptor), name);
        return new Player(playerData);
    }
}
