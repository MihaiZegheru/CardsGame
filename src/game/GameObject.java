package game;

import java.util.Set;

public abstract class GameObject {

    private GameObject parent;

    GameObject() {
        GameManager.GetInstance().RegisterGameObject(this);
        BeginPlay();
    }
    abstract void BeginPlay();
    abstract void TickRound();

    GameObject getParent() { return parent; }
    void setParent(GameObject object) { parent = object; }
}
