package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import game.Ability;
import game.ActionHandler;
import game.GameManager;
import game.WarriorType;
import game.datacollections.CasterMinionData;
import game.datacollections.DeckData;
import game.datacollections.HeroData;
import game.datacollections.MinionData;
import game.datacollections.PlayerData;
import status.StatusOr;

import java.util.ArrayList;
import java.util.Optional;

import static game.AbilityHandler.resolveAbility;
import static java.lang.System.exit;

public final class Mocker {

    /**
     * Mocks a test by creating the data for the game and feeding it.
     *
     * @param descriptor
     * @param objectMapper
     * @return ArrayNode - test results
     */
    public static ArrayNode mock(final Input descriptor, final ObjectMapper objectMapper) {
        if (descriptor == null) {
            System.out.println("Input Descriptor not loaded.");
            exit(-1);
        }

        ArrayNode arrayNode = objectMapper.createArrayNode();

        PlayerData playerOneData = buildPlayerData(descriptor.getPlayerOneDecks(), "playerOne");
        PlayerData playerTwoData = buildPlayerData(descriptor.getPlayerTwoDecks(), "playerTwo");
        GameManager.getInstance().setupTest();

        for (GameInput gameInput : descriptor.getGames()) {
            StartGameInput startGameState = gameInput.getStartGame();

            playerOneData.setHero(buildHeroData(startGameState.getPlayerOneHero()));
            int playerOneDeckIdx = startGameState.getPlayerOneDeckIdx();

            playerTwoData.setHero(buildHeroData(startGameState.getPlayerTwoHero()));
            int playerTwoDeckIdx = startGameState.getPlayerTwoDeckIdx();

            GameManager.getInstance().start(playerOneData, playerOneDeckIdx, playerTwoData,
                    playerTwoDeckIdx, startGameState.getStartingPlayer(),
                    startGameState.getShuffleSeed());

            for (ActionsInput action : gameInput.getActions()) {
                Optional<ObjectNode> node = ActionHandler.handleAction(action, objectMapper);
                if (node.isEmpty()) {
                    continue;
                }
                arrayNode.add(node.get());
            }
        }

        return arrayNode;
    }

    private static ArrayList<DeckData> buildDecksDataFromInputObject(final DecksInput input) {
        ArrayList<DeckData> decksData = new ArrayList<>(input.getNrDecks());
        for (int i = 0; i < input.getNrDecks(); ++i) {
            ArrayList<MinionData> minionsData = new ArrayList<>(input.getNrCardsInDeck());
            for (int j = 0; j < input.getNrCardsInDeck(); ++j) {
                CardInput currCardInput = input.getDecks().get(i).get(j);
                if (WarriorType.resolveWarriorType(
                        currCardInput.getName()).isAny(WarriorType.getDruid()
                        | WarriorType.getShadow())) {
                    StatusOr<Ability> abilityStatus = resolveAbility(currCardInput.getName());
                    if (!abilityStatus.isOk()) {
                        exit(-1);
                    }
                    minionsData.add(new CasterMinionData(currCardInput.getMana(),
                            currCardInput.getAttackDamage(), currCardInput.getHealth(),
                            abilityStatus.unwrap(), currCardInput.getDescription(),
                            currCardInput.getColors(), currCardInput.getName()));
                } else {
                    minionsData.add(new MinionData(currCardInput.getMana(),
                            currCardInput.getAttackDamage(), currCardInput.getHealth(),
                            currCardInput.getDescription(), currCardInput.getColors(),
                            currCardInput.getName()));
                }
            }
            decksData.add(new DeckData(minionsData));
        }
        return decksData;
    }

    private static PlayerData buildPlayerData(final DecksInput decksDescriptor, final String name) {
        return new PlayerData(buildDecksDataFromInputObject(decksDescriptor), name);
    }

    private static HeroData buildHeroData(final CardInput heroDescriptor) {
        StatusOr<Ability> abilityStatus = resolveAbility(heroDescriptor.getName());
        if (!abilityStatus.isOk()) {
            exit(-1);
        }
        return new HeroData(heroDescriptor.getMana(), abilityStatus.unwrap(),
                heroDescriptor.getDescription(), heroDescriptor.getColors(),
                heroDescriptor.getName());
    }

    private Mocker() { }
}
