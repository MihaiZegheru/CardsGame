package game;

import game.datacollections.CardData;

public final class WarriorType {

    private static final int DAMAGE_DEALER = 1;       // Deals damage
    private static final int TANK = 2;       // Takes in all damage
    private static final int DRUID = 4;       // Casts spell, doesn't directly damage enemy
    private static final int SHADOW = 8;       // Casts spells, directly damages enemy
    private static final int SUPPORT = 16;      // Targets allies
    private static final int DUELIST = 32;      // Targets enemy

    private final int mask;

    private WarriorType(final int mask) {
        this.mask = mask;
    }

    /**
     * Looks for perfect match by checking mask against provided mask.
     *
     * @param checkMask
     * @return boolean
     */
    public boolean is(final int checkMask) {
        return (this.mask & checkMask) == checkMask;
    }

    /**
     * Looks for partial match by checking mask against provided mask.
     *
     * @param checkMask
     * @return boolean
     */
    public boolean isAny(final int checkMask) {
        return (this.mask & checkMask) != 0;
    }

    /**
     * Resolves WarriorType for provided CardData.
     *
     * @param cardData
     * @return WarriorType
     */
    public static WarriorType resolveWarriorType(final CardData cardData) {
        int mask = switch (cardData.getName()) {
            case "Sentinel" -> DUELIST | DAMAGE_DEALER;
            case "Berserker" -> DUELIST | DAMAGE_DEALER;
            case "Goliath" -> DUELIST | TANK;
            case "Warden" -> DUELIST | TANK;
            case "The Cursed One" -> DUELIST | DRUID;
            case "The Ripper" -> DUELIST | SHADOW;
            case "Miraj" -> DUELIST | SHADOW;
            case "Disciple" -> SUPPORT | DRUID;
            case "Lord Royce", "Empress Thorina" -> DUELIST;
            case "King Mudface", "General Kocioraw" -> SUPPORT;
            default -> {
                System.out.println("Minion " + cardData.getName()
                        + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }

    /**
     * Resolves WarriorType for provided name.
     *
     * @param name
     * @return WarriorType
     */
    public static WarriorType resolveWarriorType(final String name) {
        int mask = switch (name) {
            case "Sentinel" -> DUELIST | DAMAGE_DEALER;
            case "Berserker" -> DUELIST | DAMAGE_DEALER;
            case "Goliath" -> DUELIST | TANK;
            case "Warden" -> DUELIST | TANK;
            case "The Cursed One" -> DUELIST | DRUID;
            case "The Ripper" -> DUELIST | SHADOW;
            case "Miraj" -> DUELIST | SHADOW;
            case "Disciple" -> SUPPORT | DRUID;
            case "Lord Royce", "Empress Thorina" -> DUELIST;
            case "King Mudface", "General Kocioraw" -> SUPPORT;
            default -> {
                System.out.println("Minion " + name + " implementation does not exist.");
                yield 0;
            }
        };
        return new WarriorType(mask);
    }

    public static int getDamageDealer() {
        return DAMAGE_DEALER;
    }

    public static int getTank() {
        return TANK;
    }

    public static int getDruid() {
        return DRUID;
    }

    public static int getShadow() {
        return SHADOW;
    }

    public static int getSupport() {
        return SUPPORT;
    }

    public static int getDuelist() {
        return DUELIST;
    }
}
