package game;

import status.Status;

import java.util.ArrayList;

interface Caster {
    Status cast(ArrayList<Minion> minions);
}
