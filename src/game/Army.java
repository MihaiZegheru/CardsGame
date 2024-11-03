package game;

import fileio.Coordinates;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Army extends GameObject {
    private ArrayList<Minion> backLine;
    private ArrayList<Minion> frontLine;
    private Hero hero;

    public Army() {
        backLine = new ArrayList<>();
        frontLine = new ArrayList<>();
    }

    // Returns Status for success or failure.
    public Status PlaceMinion(MinionData minionData) {
        ArrayList<Minion> lane;
        if (minionData.getType().isAny(WarriorType.kDamageDealer | WarriorType.kDruid)) {
            lane = backLine;
        } else if (minionData.getType().isAny(WarriorType.kTank | WarriorType.kShadow)) {
            lane = frontLine;
        } else {
            return new Status(StatusCode.kUnknown);
        }

        if (lane.size() >= GameManager.GetInstance().getCardsPerRow()) {
            return new Status(StatusCode.kOutOfRange, "Minion exceeds card limit on associated row.");
        }
        Minion minion = Minion.BuildMinion(minionData, this);
        minion.setParent(this);
        lane.add(minion);
        return Status.ok();
    }

    void OnMinionDied(Minion minion) {
        if (minion.getType().isAny(WarriorType.kDamageDealer | WarriorType.kDruid)) {
            backLine.remove(minion);
        } else if (minion.getType().isAny(WarriorType.kTank | WarriorType.kShadow)) {
          frontLine.remove(minion);
        }
    }

    void onHeroDied() {
        GameManager.GetInstance().onPlayerLost(this.getParent().<Player>getAs());
    }

    // Return the Minion at coords. Coords must be in local space. If Minion not found return Status.
    public StatusOr<Minion> getMinionAt(Coordinates coords) {
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

    public Hero getHero() { return hero; }
    public void setHero(Hero hero) { this.hero = hero; }
    public ArrayList<Minion> getBackLine() { return backLine; }
    public ArrayList<Minion> getFrontLine() { return frontLine; }
    public ArrayList<Minion> getLineAt(int idx) { return idx == 1 ? frontLine : backLine; }
    public boolean hasTanks() {
        AtomicBoolean isTank = new AtomicBoolean(false);
        frontLine.forEach((minion) -> {
            if (minion.getType().is(WarriorType.kTank)) {
                isTank.set(true);
            }
        });
        return isTank.get();
    }
    public boolean hasDamageDealers() { return !backLine.isEmpty(); }

    private Status validCoords(Coordinates coords) {
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
    void BeginPlay() {

    }

    @Override
    void TickRound() {

    }

    @Override
    void TickTurn() {

    }
}
