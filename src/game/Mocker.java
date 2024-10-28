package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;
import game.datacollections.*;

import java.util.ArrayList;

public class Mocker {
    // TODO: Return a game-end object.
    // Mock a game for the given input. This function sets up all the game variables so that it keeps the game logic
    // separate, calling its API.
    public ArrayNode Mock(Input descriptor) {
        if (descriptor == null) {
            System.out.println("Input Descriptor not loaded.");
            System.exit(-1);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        PlayerData playerOneData = BuildPlayerData(descriptor.getPlayerOneDecks(), "playerOne");
        PlayerData playerTwoData = BuildPlayerData(descriptor.getPlayerTwoDecks(), "playerTwo");

        for (GameInput gameInput : descriptor.getGames()) {
            StartGameInput startGameState = gameInput.getStartGame();

            playerOneData.setHero(BuildHeroData(startGameState.getPlayerOneHero()));
            DeckData playerOneDeck = playerOneData.getDecks().get(startGameState.getPlayerOneDeckIdx());
            Player playerOne = new Player(playerOneData, playerOneDeck);

            playerTwoData.setHero(BuildHeroData(startGameState.getPlayerTwoHero()));
            DeckData playerTwoDeck = playerTwoData.getDecks().get(startGameState.getPlayerTwoDeckIdx());
            Player playerTwo = new Player(playerTwoData, playerTwoDeck);

            GameManager.GetInstance().StartGame(playerOne, playerTwo, startGameState.getStartingPlayer(),
                    startGameState.getShuffleSeed());

            for (ActionsInput action : gameInput.getActions()) {
                arrayNode.add(ActionManager.GetInstance().HandleAction(action));
            }
        }

        // Set null after every mock.
        descriptor = null;

        return arrayNode;
    }

    private ArrayList<DeckData> BuildDecksDataFromInputObject(DecksInput input) {
        ArrayList<DeckData> decksData = new ArrayList<>(input.getNrDecks());
        for (int i = 0; i < input.getNrDecks(); ++i) {
            ArrayList<MinionData> minionsData = new ArrayList<>(input.getNrCardsInDeck());
            for (int j = 0; j < input.getNrCardsInDeck(); ++j) {
                CardInput currCardInput = input.getDecks().get(i).get(j);
                minionsData.add(new MinionData(currCardInput.getMana(), currCardInput.getAttackDamage(),
                        currCardInput.getHealth(), currCardInput.getDescription(), currCardInput.getColors(),
                        currCardInput.getName()));
            }
            decksData.add(new DeckData(minionsData));
        }
        return decksData;
    }

    private PlayerData BuildPlayerData(DecksInput decksDescriptor, String name) {
        return new PlayerData(BuildDecksDataFromInputObject(decksDescriptor), name);
    }

    private HeroData BuildHeroData(CardInput heroDescriptor) {
        return new HeroData(heroDescriptor.getMana(), heroDescriptor.getDescription(), heroDescriptor.getColors(),
                heroDescriptor.getName());
    }

}
