package game;


import fileio.Coordinates;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;

/**
 * Game class acts as a middle man between the 2 battling players.
 */
public final class Game extends GameObject {

    private Player playerOne;
    private Player playerTwo;

    @Override
    void beginPlay() {
        playerOne = GameManager.getInstance().getPlayerById(1).unwrap();
        playerTwo = GameManager.getInstance().getPlayerById(2).unwrap();
    }

    @Override
    void tickRound() {
    }

    @Override
    void tickTurn() {
    }

    /**
     * Tries to perform an attack at defenderCoords using minion.
     *
     * @param attacker Player that is attacking
     * @param minion Attacking minion
     * @param defenderCoords Target position
     * @return Status
     */
    public Status attackAt(final Player attacker, final Minion minion,
                           final Coordinates defenderCoords) {
        if (!defenderCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, "Attacked card does not belong to the enemy.");
        }

        Player defender = GameManager.getInstance().getOtherPlayer(attacker);
        StatusOr<Minion> defenderMinionStatus = defender.getArmy().getMinionAt(defenderCoords);
        if (!defenderMinionStatus.isOk()) {
            return defenderMinionStatus;
        }
        Minion defenderMinion = defenderMinionStatus.unwrap();
        if (defender.getArmy().hasTanks() && !defenderMinion.getType().is(WarriorType.getTank())) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        return minion.attack(defenderMinion);
    }

    /**
     * Tries to perform an attack on the enemy hero of attacker using minion.
     *
     * @param attacker Player that is attacking
     * @param minion Attacking minion
     * @return Status
     */
    public Status attackHero(final Player attacker, final Minion minion) {
        Player defender = GameManager.getInstance().getOtherPlayer(attacker);
        Hero enemyHero = defender.getArmy().getHero();
        if (defender.getArmy().hasTanks()) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        return minion.attack(enemyHero);
    }

    /**
     * Tries to cast/use an ability on targetCoords from minion of caster.
     *
     * @param caster Player that is casting
     * @param minion Casting minion
     * @param targetCoords Target position
     * @return Status
     */
    public Status castAt(final Player caster, final Minion minion, final Coordinates targetCoords) {
        if (minion.getData().getType().is(WarriorType.getDuelist())
                != targetCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, minion.getType().is(WarriorType.getDuelist())
                    ? "Attacked card does not belong to the enemy."
                    : "Attacked card does not belong to the current player.");
        }

        Player target = targetCoords.getIsEnemyPosition()
                ? GameManager.getInstance().getOtherPlayer(caster)
                : caster;
        StatusOr<Minion> targetMinionStatus = target.getArmy().getMinionAt(targetCoords);
        if (!targetMinionStatus.isOk()) {
            return targetMinionStatus;
        }

        Minion targetMinion = targetMinionStatus.unwrap();
        if (!minion.getType().is(WarriorType.getDruid()) && target.getArmy().hasTanks()
                && !targetMinion.getType().is(WarriorType.getTank())) {
            return new Status(StatusCode.kAborted, "Attacked card is not of type 'Tank'.");
        }
        ArrayList<Minion> targetMinions = new ArrayList<>();
        targetMinions.add(targetMinion);
        return minion.<CasterMinion>getAs().cast(targetMinions);
    }

    /**
     * Tries to perform a hero cast.
     *
     * @param caster Player that is casting
     * @param hero Casting hero
     * @param targetCoords Target position should be a row
     * @return Status
     */
    public Status heroCastAt(final Player caster, final Hero hero, final Coordinates targetCoords) {
        if (hero.getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Hero has already attacked this turn.");
        }
        if (hero.getData().getType().is(WarriorType.getDuelist())
                != targetCoords.getIsEnemyPosition()) {
            return new Status(StatusCode.kAborted, hero.getType().is(WarriorType.getDuelist())
                    ? "Selected row does not belong to the enemy."
                    : "Selected row does not belong to the current player.");
        }
        Player target = targetCoords.getIsEnemyPosition()
                ? GameManager.getInstance().getOtherPlayer(caster)
                : caster;
        return hero.<Hero>getAs().cast(target.getArmy().getLineAt(targetCoords.getX()));
    }

    /**
     * Concatenates armies.
     *
     * @return Board
     */
    public ArrayList<ArrayList<Minion>> getBoard() {
        ArrayList<ArrayList<Minion>> board = new ArrayList<>();
        board.add(playerTwo.getArmy().getBackLine());
        board.add(playerTwo.getArmy().getFrontLine());
        board.add(playerOne.getArmy().getFrontLine());
        board.add(playerOne.getArmy().getBackLine());
        return board;
    }
}
