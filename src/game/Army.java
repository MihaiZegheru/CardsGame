package game;

import fileio.Coordinates;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Army extends GameObject {
    private ArrayList<Minion> backLine;
    private ArrayList<Minion> frontLine;
    private Hero hero;

    public Army() {
        backLine = new ArrayList<>();
        frontLine = new ArrayList<>();
    }

    /**
     * Tries placing a Minion based on provided MinionData.
     *
     * @implNote  Used provided MinionData to construct a new Minion.
     *
     * @param minionData
     * @return Status
     */
    Status placeMinion(final MinionData minionData) {
        ArrayList<Minion> lane;
        if (minionData.getType().isAny(WarriorType.getDamageDealer() | WarriorType.getDruid())) {
            lane = backLine;
        } else if (minionData.getType().isAny(WarriorType.getTank() | WarriorType.getShadow())) {
            lane = frontLine;
        } else {
            return new Status(StatusCode.kUnknown);
        }

        if (lane.size() >= GameManager.getInstance().getCardsPerRow()) {
            return new Status(StatusCode.kOutOfRange,
                    "Minion exceeds card limit on associated row.");
        }
        Minion minion = Minion.buildMinion(minionData, this);
        minion.setParent(this);
        lane.add(minion);
        return Status.ok();
    }

    void onMinionDied(final Minion minion) {
        if (minion.getType().isAny(WarriorType.getDamageDealer()
                | WarriorType.getDruid())) {
            backLine.remove(minion);
        } else if (minion.getType().isAny(WarriorType.getTank()
                | WarriorType.getShadow())) {
            frontLine.remove(minion);
        }
    }

    void onHeroDied() {
        GameManager.getInstance().onPlayerLost(this.getParent().<Player>getAs());
    }

    boolean hasTanks() {
        AtomicBoolean isTank = new AtomicBoolean(false);
        frontLine.forEach((minion) -> {
            if (minion.getType().is(WarriorType.getTank())) {
                isTank.set(true);
            }
        });
        return isTank.get();
    }

    /**
     * Tries returning the Minion at the provided Coordinates.
     *
     * @param coords
     * @return
     */
    StatusOr<Minion> getMinionAt(final Coordinates coords) {
        Status coordsStatus = validCoords(coords);
        if (!coordsStatus.isOk()) {
            return new StatusOr<>(coordsStatus);
        }

        if (coords.getX() == 0) {
            return new StatusOr<>(backLine.get(coords.getY()));
        }
        if (coords.getX() == 1) {
            return new StatusOr<>(frontLine.get(coords.getY()));
        }
        return new StatusOr<>(StatusCode.kUnknown);
    }

    Hero getHero() {
        return hero;
    }

    void setHero(final Hero hero) {
        this.hero = hero;
    }

    ArrayList<Minion> getBackLine() {
        return backLine;
    }

    ArrayList<Minion> getFrontLine() {
        return frontLine;
    }

    ArrayList<Minion> getLineAt(final int idx) {
        return idx == 1 ? frontLine : backLine;
    }

    private Status validCoords(final Coordinates coords) {
        if (coords.getX() > 1) {
            return new Status(StatusCode.kOutOfRange, "Coordinated do not belong to you.");
        } else if (coords.getX() == 0 && backLine.size() <= coords.getY()) {
            return new Status(StatusCode.kAborted, "No card available at that position.");
        } else if (coords.getX() == 1 && frontLine.size() <= coords.getY()) {
            return new Status(StatusCode.kAborted, "No card available at that position.");
        }
        return Status.ok();
    }

    @Override
    void beginPlay() {

    }

    @Override
    void tickRound() {

    }

    @Override
    void tickTurn() {

    }
}
