package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.exit;

public final class ActionHandler {

    /**
     * Performs the given command.
     *
     * @param action
     * @param objectMapper
     * @return Optional ObjectNode
     */
    public static Optional<ObjectNode> handleAction(final ActionsInput action,
                                                    final ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", action.getCommand());

        return switch (action.getCommand()) {
            case "cardUsesAbility" -> Optional.ofNullable(cardUsesAbility(action, objectNode));
            case "cardUsesAttack" -> Optional.ofNullable(cardUsesAttack(action, objectNode));
            case "getCardAtPosition" -> Optional.of(getCardAtPosition(action, objectNode));
            case "getCardsInHand" -> Optional.of(getCardsInHand(action, objectNode));
            case "getCardsOnTable" -> Optional.of(getCardsOnTable(action, objectNode));
            case "getFrozenCardsOnTable" -> Optional.of(getFrozenCardsOnTable(action, objectNode));
            case "getTotalGamesPlayed" -> Optional.of(getGamesPlayed(action, objectNode));
            case "getPlayerDeck" -> Optional.of(getPlayerDeck(action, objectNode));
            case "getPlayerHero" -> Optional.of(getPlayerHero(action, objectNode));
            case "getPlayerMana" -> Optional.of(getPlayerMana(action, objectNode));
            case "getPlayerOneWins" -> Optional.of(getplayeronewins(action, objectNode));
            case "getPlayerTurn" -> Optional.of(getPlayerTurn(objectNode));
            case "endPlayerTurn" -> {
                GameManager.getInstance().endPlayerTurn();
                yield Optional.empty();
            }
            case "getPlayerTwoWins" -> Optional.of(getPlayerTwoWins(action, objectNode));
            case "placeCard" -> Optional.ofNullable(placeCard(action, objectNode));
            case "useAttackHero" -> Optional.ofNullable(cardAttackHero(action, objectNode));
            case "useHeroAbility" -> Optional.ofNullable(heroUsesAbility(action, objectNode));
            default -> {
                System.out.println("Action " + action.getCommand() + " not implemented yet.");
                yield Optional.empty();
            }
        };
    }

    private static ObjectNode getPlayerDeck(final ActionsInput action,
                                            final ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.getInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(player.unwrap().getDeckCardsData());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private static ObjectNode getPlayerHero(final ActionsInput action,
                                            final ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.getInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output",
                objectMapper.valueToTree(player.unwrap().getArmy().getHero()));
        return objectNode;
    }

    private static ObjectNode getPlayerTurn(final ObjectNode objectNode) {
        objectNode.put("output", GameManager.getInstance().getActivePlayerId());
        return objectNode;
    }

    private static ObjectNode getCardsInHand(final ActionsInput action,
                                             final ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.getInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output",
                objectMapper.valueToTree(player.unwrap().getCardsInHand()));
        return objectNode;
    }

    private static ObjectNode getPlayerMana(final ActionsInput action,
                                            final ObjectNode objectNode) {
        StatusOr<Player> player = GameManager.getInstance().getPlayerById(action.getPlayerIdx());
        if (!player.hasBody()) {
            exit(-1);
        }
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", player.unwrap().getCurrMana());
        return objectNode;
    }

    private static ObjectNode getCardsOnTable(final ActionsInput action,
                                              final ObjectNode objectNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode =
                objectMapper.valueToTree(GameManager.getInstance().getGame().getBoard());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private static ObjectNode placeCard(final ActionsInput action, final ObjectNode objectNode) {
        Status response = GameManager.getInstance().placeCard(action.getHandIdx());
        if (response.isOk()) {
            return null;
        }
        objectNode.put("handIdx", action.getHandIdx());
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode cardUsesAttack(final ActionsInput action,
                                             final ObjectNode objectNode) {
        Status response = GameManager.getInstance().useMinionAttack(action.getCardAttacker(),
                action.getCardAttacked());
        if (response.isOk()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker",
                objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("cardAttacked",
                objectMapper.valueToTree(action.getCardAttacked()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode getCardAtPosition(final ActionsInput action,
                                                final ObjectNode objectNode) {
        StatusOr<Minion> response =
                GameManager.getInstance().getCardAt(new Coordinates(action.getX(), action.getY()));
        objectNode.put("x", action.getX());
        objectNode.put("y", action.getY());
        if (!response.isOk()) {
            objectNode.put("output", response.toString());
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            objectNode.put("output", objectMapper.valueToTree(response.unwrap()));
        }
        return objectNode;
    }

    private static ObjectNode cardUsesAbility(final ActionsInput action,
                                              final ObjectNode objectNode) {
        Status response = GameManager.getInstance().useMinionAbility(action.getCardAttacker(),
                action.getCardAttacked());
        if (response.isOk()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker",
                objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("cardAttacked",
                objectMapper.valueToTree(action.getCardAttacked()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode cardAttackHero(final ActionsInput action,
                                             final ObjectNode objectNode) {
        Status response = GameManager.getInstance().useMinionAttackHero(action.getCardAttacker());
        if (response.isOk()) {
            return null;
        } else if (response.equals(new Status(StatusCode.kEnded))) {
            objectNode.remove("command");
            objectNode.put("gameEnded", response.toString());
            return objectNode;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("cardAttacker",
                objectMapper.valueToTree(action.getCardAttacker()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode heroUsesAbility(final ActionsInput action,
                                              final ObjectNode objectNode) {
        Coordinates coords = new Coordinates(action.getAffectedRow(), 0);
        Status response = GameManager.getInstance().useHeroAbility(coords);
        if (response.isOk()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("affectedRow",
                objectMapper.valueToTree(action.getAffectedRow()));
        objectNode.put("error", response.toString());
        return objectNode;
    }

    private static ObjectNode getFrozenCardsOnTable(final ActionsInput action,
                                                    final ObjectNode objectNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<ArrayList<Minion>> minions = GameManager.getInstance().getGame().getBoard();
        ArrayList<Minion> frozenMinions = new ArrayList<>();
        for (ArrayList<Minion> row : minions) {
            for (Minion minion : row) {
                if (minion.getIsFrozen()) {
                    frozenMinions.add(minion);
                }
            }
        }
        ArrayNode arrayNode = objectMapper.valueToTree(frozenMinions);
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private static ObjectNode getplayeronewins(final ActionsInput action,
                                               final ObjectNode objectNode) {
        objectNode.put("output", GameManager.getInstance().getPlayerOneWins());
        return objectNode;
    }

    private static ObjectNode getPlayerTwoWins(final ActionsInput action,
                                               final ObjectNode objectNode) {
        objectNode.put("output", GameManager.getInstance().getPlayerTwoWins());
        return objectNode;
    }

    private static ObjectNode getGamesPlayed(final ActionsInput action,
                                             final ObjectNode objectNode) {
        objectNode.put("output", GameManager.getInstance().getGamesPlayed());
        return objectNode;
    }

    private ActionHandler() { }
}

