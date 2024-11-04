package game;

public abstract class GameObject {

    private GameObject parent;

    GameObject() {
        GameManager.getInstance().registerGameObject(this);
    }

    /**
     * Triggered on game start.
     */
    abstract void beginPlay();

    /**
     * Tick object every new round.
     */
    abstract void tickRound();

    /**
     * Tick object every new turn.
     */
    abstract void tickTurn();

    final <T> T getAs() {
        return (T) this;
    }

    final GameObject getParent() {
        return parent;
    }

    final void setParent(final GameObject object) {
        parent = object;
    }
}
