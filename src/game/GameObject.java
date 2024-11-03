package game;

import java.util.Set;

public abstract class GameObject {

    private GameObject parent;

    GameObject() {
        GameManager.GetInstance().RegisterGameObject(this);
    }
    abstract void BeginPlay();
    abstract void TickRound();
    abstract void TickTurn();

    <T> T getAs() {
        return (T)this;
    }

    GameObject getParent() { return parent; }
    void setParent(GameObject object) { parent = object; }
}
