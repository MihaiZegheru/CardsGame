package game;


import fileio.Coordinates;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;

public class Game extends GameObject{

    private Player playerOne;
    private Player playerTwo;

    @Override
    void BeginPlay() {
        playerOne = GameManager.GetInstance().getPlayerById(1).unwrap();
        playerTwo = GameManager.GetInstance().getPlayerById(2).unwrap();
    }

    @Override
    void TickRound() {}

    public Status AttackAt(Player attacker, Minion minion, Coordinates defenderCoords) {
        System.out.println(minion.getData().getName());
        if (minion.getData().getType().is(WarriorType.kAttacker) != defenderCoords.getIsEnemyPosition()) {
            System.out.println("GOT YOU");
            return new Status(StatusCode.kAborted, defenderCoords.getIsEnemyPosition()
                    ? "Attacked card does not belong to the enemy."
                    : "Attacked card does not belong to the current player.");
        }

        Player defender;
        if (attacker == playerOne) {
            defender = playerTwo;
        } else {
            defender = playerOne;
        }

        StatusOr<Minion> defenderMinionStatus = defender.getArmy().getMinionAt(defenderCoords);
        if (!defenderMinionStatus.isOk()) {
            return defenderMinionStatus;
        }

        Minion defenderMinion = defenderMinionStatus.unwrap();
        if (defender.getArmy().hasTanks() && !defenderMinion.isTank()) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank’.");
        }
        return minion.Attack(defenderMinion);
    }

    public ArrayList<ArrayList<Minion>> getBoard() {
        ArrayList<ArrayList<Minion>> board = new ArrayList<>();
        board.add(playerTwo.getArmy().getDdLane());
        board.add(playerTwo.getArmy().getTankLane());
        board.add(playerOne.getArmy().getTankLane());
        board.add(playerOne.getArmy().getDdLane());
        return board;
    }


}
