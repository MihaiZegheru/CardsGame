package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import status.Status;

import java.util.Optional;

public class ActionManager {
    private static ActionManager instance = null;
    public static ActionManager GetInstance() {
        if (instance == null) {
            instance = new ActionManager();
        }
        return instance;
    }

    public Optional<ObjectNode> HandleAction(ActionsInput action, ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", action.getCommand());

        return switch (action.getCommand()) {
            case "cardUsesAttack" -> Optional.ofNullable(CardUsesAttack(action, objectNode));
            case "getCardsInHand" -> Optional.of(GetCardsInHand(action, objectNode));
            case "getCardsOnTable" -> Optional.of(GetCardsOnTable(action, objectNode));
            case "getPlayerDeck" -> Optional.of(GetPlayerDeck(action, objectNode));
            case "getPlayerHero" -> Optional.of(GetPlayerHero(action, objectNode));
            case "getPlayerMana" -> Optional.of(GetPlayerMana(action, objectNode));
            case "getPlayerTurn" -> Optional.of(GetPlayerTurn(objectNode));
            case "endPlayerTurn" -> {
                GameManager.GetInstance().EndPlayerTurn();
                yield Optional.empty();
            }
            case "placeCard" -> Optional.ofNullable(PlaceCard(action, objectNode));
            default -> {
                System.out.println("Action " + action.getCommand() + " not implemented yet.");
                yield Optional.empty();
            }
        };
    }

    private ObjectNode GetPlayerDeck(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().getPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(player.getDeckCardsData());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private ObjectNode GetPlayerHero(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().getPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", objectMapper.valueToTree(player.getHeroData()));
        return objectNode;
    }

    private ObjectNode GetPlayerTurn(ObjectNode objectNode) {
        objectNode.put("output", GameManager.GetInstance().getPlayerAtTurnId());
        return objectNode;
    }

    private ObjectNode GetCardsInHand(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().getPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", objectMapper.valueToTree(player.getCardsInHand()));
        return objectNode;
    }

    private ObjectNode GetPlayerMana(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().getPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", player.getCurrMana());
        return objectNode;
    }

    private ObjectNode GetCardsOnTable(ActionsInput action, ObjectNode objectNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(GameManager.GetInstance().getGame().getBoard());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private ObjectNode PlaceCard(ActionsInput action, ObjectNode objectNode) {
        Status response = GameManager.GetInstance().PlaceCard(action.getHandIdx());
        if (response.isOk()) {
            return null;
        }
        objectNode.put("handIdx", action.getHandIdx());
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private ObjectNode CardUsesAttack(ActionsInput action, ObjectNode objectNode) {
        Status response = GameManager.GetInstance().UseMinionAttack(action.getCardAttacker(), action.getCardAttacked());
        if (response.isOk()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker", objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("cardAttacked", objectMapper.valueToTree(action.getCardAttacked()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

}
