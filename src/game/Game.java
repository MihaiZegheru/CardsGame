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
        if (!defenderCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, "Attacked card does not belong to the enemy.");
        }

        Player defender = GameManager.GetInstance().getOtherPlayer(attacker);
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

    public Status attackHero(Player attacker, Minion minion) {
        Player defender = GameManager.GetInstance().getOtherPlayer(attacker);
        Hero enemyHero = defender.getArmy().getHero();
        System.out.println(defender.getArmy());
//        if (defender.getArmy().hasTanks()) {
//            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank’.");
//        }
        return minion.Attack(enemyHero);
    }

    public Status CastAt(Player caster, Minion minion, Coordinates targetCoords) {
        if (minion.getData().getType().is(WarriorType.kAttacker) != targetCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, targetCoords.getIsEnemyPosition()
                    ? "Attacked card does not belong to the enemy."
                    : "Attacked card does not belong to the current player.");
        }

        Player target = targetCoords.getIsEnemyPosition()
                ? GameManager.GetInstance().getOtherPlayer(caster)
                : caster;
        StatusOr<Minion> targetMinionStatus = target.getArmy().getMinionAt(targetCoords);
        if (!targetMinionStatus.isOk()) {
            return targetMinionStatus;
        }

        Minion targetMinion = targetMinionStatus.unwrap();
        return minion.<CasterMinion>getAs().cast(targetMinion);
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
