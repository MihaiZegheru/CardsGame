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

    @Override
    void TickTurn() {}

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
        if (defender.getArmy().hasTanks() && !defenderMinion.getType().is(WarriorType.kTank)) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        return minion.Attack(defenderMinion);
    }

    public Status attackHero(Player attacker, Minion minion) {
        Player defender = GameManager.GetInstance().getOtherPlayer(attacker);
        Hero enemyHero = defender.getArmy().getHero();
        if (defender.getArmy().hasTanks()) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        return minion.Attack(enemyHero);
    }

    public Status CastAt(Player caster, Minion minion, Coordinates targetCoords) {
        if (minion.getData().getType().is(WarriorType.kDuelist) != targetCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, minion.getType().is(WarriorType.kDuelist)
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
        if (!minion.getType().is(WarriorType.kDruid) && target.getArmy().hasTanks() &&
                !targetMinion.getType().is(WarriorType.kTank)) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        ArrayList<Minion> targetMinions = new ArrayList<>();
        targetMinions.add(targetMinion);
        return minion.<CasterMinion>getAs().cast(targetMinions);
    }

    public Status HeroCastAt(Player caster, Hero hero, Coordinates targetCoords) {
        // TODO: Remove check after submission
        if (hero.getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Hero has already attacked this turn.");
        }
        if (hero.getData().getType().is(WarriorType.kDuelist) != targetCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, hero.getType().is(WarriorType.kDuelist)
                    ? "Selected row does not belong to the enemy."
                    : "Selected row does not belong to the current player.");
        }
        Player target = targetCoords.getIsEnemyPosition()
                ? GameManager.GetInstance().getOtherPlayer(caster)
                : caster;
        return hero.<Hero>getAs().cast(target.getArmy().getLineAt(targetCoords.getX()));
    }

    public ArrayList<ArrayList<Minion>> getBoard() {
        ArrayList<ArrayList<Minion>> board = new ArrayList<>();
        board.add(playerTwo.getArmy().getBackLine());
        board.add(playerTwo.getArmy().getFrontLine());
        board.add(playerOne.getArmy().getFrontLine());
        board.add(playerOne.getArmy().getBackLine());
        return board;
    }


}
