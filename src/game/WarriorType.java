package game;

import game.datacollections.CardData;

public class WarriorType {
    public static int kDamageDealer = 1;   // Sits on back row
    public static int kTank = 2;           // Sits on front row
    public static int kSupport = 4;        // Targets allies
    public static int kAttacker = 8;       // Targets enemy
    ;
    private final int mask;

    private WarriorType(int mask) {
        this.mask = mask;
    }

    public boolean is(int mask) {
        System.out.println(this.mask + " " + mask);
        return (this.mask & mask) == mask;
    }

    public static WarriorType ResolveWarriorType(CardData minionData) {
        int a = switch (minionData.getName()) {
            case "Sentinel", "Berserker", "The Cursed One", "Disciple" -> kDamageDealer;
            case "Goliath", "Warden", "The Ripper", "Miraj" -> kTank;
            default -> {
                System.out.println("Minion " + minionData.getName() + " implementation does not exist.");
                yield -1;
            }
        };
        int b = switch (minionData.getName()) {
            case "Sentinel", "Berserker", "Goliath", "Warden", "The Cursed One", "The Ripper", "Miraj"-> kAttacker;
            case "Disciple" -> kSupport;
            default -> {
                yield -1;
            }
        };
        return new WarriorType(a | b);
    }
}
