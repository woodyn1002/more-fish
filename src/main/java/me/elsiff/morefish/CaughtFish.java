package me.elsiff.morefish;

import org.bukkit.OfflinePlayer;

public class CaughtFish extends CustomFish {
    private final double length;
    private final OfflinePlayer catcher;

    public CaughtFish(CustomFish fish, double length, OfflinePlayer catcher) {
        super(fish.getName(), fish.getLore(), fish.getLengthMin(), fish.getLengthMax(), fish.getIcon(),
                fish.hasNoItemFormat(), fish.getCommands(), fish.getFoodEffects(), fish.getRarity());

        this.length = length;
        this.catcher = catcher;
    }

    public double getLength() {
        return length;
    }

    public OfflinePlayer getCatcher() {
        return catcher;
    }
}
