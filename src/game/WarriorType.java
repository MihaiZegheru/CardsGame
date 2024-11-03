package game;

import game.datacollections.CardData;

import static java.lang.System.exit;

public class WarriorType {
    public static int kDamageDealer  = 1;       // Deals damage
    public static int kTank          = 2;       // Takes in all damage
    public static int kDruid         = 4;       // Casts spell, doesn't directly damage enemy
    public static int kShadow        = 8;       // Casts spells, directly damages enemy

    public static int kSupport       = 16;      // Targets allies
    public static int kDuelist       = 32;      // Targets enemy
    ;
    private final int mask;

    private WarriorType(int mask) {
        this.mask = mask;
    }

    public boolean is(int mask) {
        return (this.mask & mask) == mask;
    }

    public boolean isAny(int mask) {
        return (this.mask & mask) != 0;
    }

    public static WarriorType ResolveWarriorType(CardData minionData) {
        int mask = switch (minionData.getName()) {
            case "Sentinel" -> kDuelist | kDamageDealer;
            case "Berserker" -> kDuelist | kDamageDealer;
            case "Goliath" -> kDuelist | kTank;
            case "Warden" -> kDuelist | kTank;
            case "The Cursed One" -> kDuelist | kDruid;
            case "The Ripper" -> kDuelist | kShadow;
            case "Miraj" -> kDuelist | kShadow;
            case "Disciple" -> kSupport | kDruid;
            case "Lord Royce",
                 "Empress Thorina" -> kDuelist;
            case "King Mudface",
                 "General Kocioraw" -> kSupport;
            default ->  {
                System.out.println("Minion " + minionData.getName() + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }

    public static WarriorType ResolveWarriorType(String minionName) {
        int mask = switch (minionName) {
            case "Sentinel" -> kDuelist | kDamageDealer;
            case "Berserker" -> kDuelist | kDamageDealer;
            case "Goliath" -> kDuelist | kTank;
            case "Warden" -> kDuelist | kTank;
            case "The Cursed One" -> kDuelist | kDruid;
            case "The Ripper" -> kDuelist | kShadow;
            case "Miraj" -> kDuelist | kShadow;
            case "Disciple" -> kSupport | kDruid;
            case "Lord Royce",
                 "Empress Thorina" -> kDuelist;
            case "King Mudface",
                 "General Kocioraw" -> kSupport;
            default ->  {
                System.out.println("Minion " + minionName + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }
}
