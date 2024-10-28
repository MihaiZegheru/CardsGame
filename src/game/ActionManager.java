package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.io.File;

public class ActionManager {
    private static ActionManager instance = null;
    public static ActionManager GetInstance() {
        if (instance == null) {
            instance = new ActionManager();
        }
        return instance;
    }

    public ObjectNode HandleAction(ActionsInput action) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", action.getCommand());

        switch (action.getCommand()) {
            case "getPlayerDeck":
                return GetPlayerDeck(action, objectNode);
            case "getPlayerHero":
                return GetPlayerHero(action, objectNode);
            default:
                System.out.println("Action " + action.getCommand() + " not implemented yet.");
        }
        return objectNode;
    }

    private ObjectNode GetPlayerDeck(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().GetPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(player.getDeckCardsData());
        objectNode.put("output", arrayNode);
        return objectNode;
    }

    private ObjectNode GetPlayerHero(ActionsInput action, ObjectNode objectNode) {
        Player player = GameManager.GetInstance().GetPlayer(action.getPlayerIdx());
        objectNode.put("playerIdx", action.getPlayerIdx());
        ObjectMapper objectMapper = new ObjectMapper();
        objectNode.put("output", objectMapper.valueToTree(player.getHeroData()));
        return objectNode;
    }
}
