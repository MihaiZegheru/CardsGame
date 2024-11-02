package game;

import fileio.Coordinates;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import status.StatusOr;

import java.util.ArrayList;

public class Army extends GameObject {
    private ArrayList<Minion> ddLane;
    private ArrayList<Minion> tankLane;
    private Hero hero;

    public Army() {
        ddLane = new ArrayList<>();
        tankLane = new ArrayList<>();
    }

    // Returns Status for success or failure.
    public Status PlaceMinion(MinionData minionData) {
        ArrayList<Minion> lane;
        if (minionData.getType().is(WarriorType.kDamageDealer)) {
            lane = ddLane;
        } else if (minionData.getType().is(WarriorType.kTank)) {
            lane = tankLane;
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
        if (minion.isDd()) {
            ddLane.remove(minion);
        } else if (minion.isTank()) {
          tankLane.remove(minion);
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
            return new StatusOr<>(ddLane.get(coords.getY()));
        }
        if (coords.getX() == 1) {
            return new StatusOr<>(tankLane.get(coords.getY()));
        }
        return new StatusOr<>(StatusCode.kUnknown);
    }

    public Hero getHero() { return hero; }
    public void setHero(Hero hero) { this.hero = hero; }
    public ArrayList<Minion> getDdLane() { return ddLane; }
    public ArrayList<Minion> getTankLane() { return tankLane; }
    public boolean hasTanks() { return !tankLane.isEmpty(); }
    public boolean hasDamageDealers() { return !ddLane.isEmpty(); }

    private Status validCoords(Coordinates coords) {
        if (coords.getX() > 1) {
            return new Status(StatusCode.kOutOfRange, "Coordinated do not belong to you.");
        } else if (coords.getX() == 0 && ddLane.size() <= coords.getY()) {
            return new Status(StatusCode.kAborted, "No card available at that position.");
        } else if (coords.getX() == 1 && tankLane.size() <= coords.getY()) {
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
}
