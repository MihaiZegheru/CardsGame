package game;

import game.datacollections.CardData;
import game.datacollections.MinionData;

import java.util.ArrayList;

public class Minion extends Card {
    protected int attackDamage;

    public Minion(MinionData data) {
        super(data);
        this.attackDamage = data.getAttackDamage();
    }

    @Override
    protected void OnDied() {
        GameManager.GetInstance().getGame().OnMinionDeath(this);
    }

    public int getAttackDamage() { return attackDamage; }
}
