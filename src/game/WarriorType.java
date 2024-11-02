package game;

import game.datacollections.CardData;

import static java.lang.System.exit;

public class WarriorType {
    public static int kDamageDealer = 1;   // Sits on back row
    public static int kTank = 2;           // Sits on front row
    public static int kSupport = 4;        // Targets allies
    public static int kAttacker = 8;       // Targets enemy
    public static int kCaster = 16;       // Targets enemy
    ;
    private final int mask;

    private WarriorType(int mask) {
        this.mask = mask;
    }

    public boolean is(int mask) {
        return (this.mask & mask) == mask;
    }

    public static WarriorType ResolveWarriorType(CardData minionData) {
        int mask = switch (minionData.getName()) {
            case "Sentinel" -> kAttacker | kDamageDealer;
            case "Berserker" -> kAttacker | kDamageDealer;
            case "Goliath" -> kAttacker | kTank;
            case "Warden" -> kAttacker | kTank;
            case "The Cursed One" -> kAttacker | kDamageDealer | kCaster;
            case "The Ripper" -> kAttacker | kTank | kCaster;
            case "Miraj" -> kAttacker | kTank | kCaster;
            case "Disciple" -> kSupport | kDamageDealer | kCaster;
            default ->  {
                System.out.println("Minion " + minionData.getName() + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }

    public static WarriorType ResolveWarriorType(String minionName) {
        int mask = switch (minionName) {
            case "Sentinel" -> kAttacker | kDamageDealer;
            case "Berserker" -> kAttacker | kDamageDealer;
            case "Goliath" -> kAttacker | kTank;
            case "Warden" -> kAttacker | kTank;
            case "The Cursed One" -> kAttacker | kDamageDealer | kCaster;
            case "The Ripper" -> kAttacker | kTank | kCaster;
            case "Miraj" -> kAttacker | kTank | kCaster;
            case "Disciple" -> kSupport | kDamageDealer | kCaster;
            default ->  {
                System.out.println("Minion " + minionName + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }
}
