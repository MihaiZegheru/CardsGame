package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.Optional;

import static java.lang.System.exit;

public class ActionManager {

    public static Optional<ObjectNode> HandleAction(ActionsInput action, ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", action.getCommand());

        return switch (action.getCommand()) {
            case "cardUsesAbility" -> Optional.ofNullable(CardUsesAbility(action, objectNode));
            case "cardUsesAttack" -> Optional.ofNullable(CardUsesAttack(action, objectNode));
            case "getCardAtPosition" -> Optional.of(GetCardAtPosition(action, objectNode));
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
            case "useAttackHero" -> Optional.ofNullable(CardAttackHero(action, objectNode));
            default -> {
                System.out.println("Action " + action.getCommand() + " not implemented yet.");
                yield Optional.empty();
            }
        };
    }

    private static ObjectNode GetPlayerDeck(ActionsInput action, ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.GetInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(player.unwrap().getDeckCardsData());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private static ObjectNode GetPlayerHero(ActionsInput action, ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.GetInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", objectMapper.valueToTree(player.unwrap().getArmy().getHero()));
        return objectNode;
    }

    private static ObjectNode GetPlayerTurn(ObjectNode objectNode) {
        objectNode.put("output", GameManager.GetInstance().getActivePlayerId());
        return objectNode;
    }

    private static ObjectNode GetCardsInHand(ActionsInput action, ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.GetInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", objectMapper.valueToTree(player.unwrap().getCardsInHand()));
        return objectNode;
    }

    private static ObjectNode GetPlayerMana(ActionsInput action, ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.GetInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", player.unwrap().getCurrMana());
        return objectNode;
    }

    private static ObjectNode GetCardsOnTable(ActionsInput action, ObjectNode objectNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(GameManager.GetInstance().getGame().getBoard());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private static ObjectNode PlaceCard(ActionsInput action, ObjectNode objectNode) {
        Status response = GameManager.GetInstance().PlaceCard(action.getHandIdx());
        if (response.isOk()) {
            return null;
        }
        objectNode.put("handIdx", action.getHandIdx());
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode CardUsesAttack(ActionsInput action, ObjectNode objectNode) {
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

    private static ObjectNode GetCardAtPosition(ActionsInput action, ObjectNode objectNode) {
        StatusOr<Minion> response = GameManager.GetInstance().GetCardAt(new Coordinates(action.getX(), action.getY()));
        objectNode.put("x", action.getX());
        objectNode.put("y",  action.getY());
        if (!response.isOk()) {
            objectNode.put("output", response.toString());
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            objectNode.put("output", objectMapper.valueToTree(response.unwrap()));
        }
        return objectNode;
    }

    private static ObjectNode CardUsesAbility(ActionsInput action, ObjectNode objectNode) {
        Status response = GameManager.GetInstance().useMinionAbility(action.getCardAttacker(),
                action.getCardAttacked());
        if (response.isOk()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker", objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("cardAttacked", objectMapper.valueToTree(action.getCardAttacked()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode CardAttackHero(ActionsInput action, ObjectNode objectNode) {
        Status response = GameManager.GetInstance().useMinionAttackHero(action.getCardAttacker());
        if (response.isOk()) {
            return null;
        } else if (response.equals(new Status(StatusCode.kEnded))) {
            objectNode.remove("command");
            objectNode.put("gameEnded", response.toString());
            return objectNode;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker", objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("error", response.toString());
        return objectNode;
    }
}
