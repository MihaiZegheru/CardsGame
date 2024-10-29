package game;

import game.datacollections.MinionData;

public enum MinionType {
    kNone,
    kDamageDealer,
    kTank,
    ;

    public static MinionType ResolveMinionType(MinionData minionData) {
        return switch (minionData.getName()) {
            case "Sentinel", "Berserker", "The Cursed One", "Disciple" -> MinionType.kDamageDealer;
            case "Goliath", "Warden", "The Ripper", "Miraj" -> MinionType.kTank;
            default -> {
                System.out.println("Minion " + minionData.getName() + " implementation does not exist.");
                yield kNone;
            }
        };
    }
}
