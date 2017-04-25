package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;

public class RainingCondition implements Condition {
    public boolean raining;

    public RainingCondition(boolean raining) {
        this.raining = raining;
    }

    @Override
    public boolean isSatisfying(Player player) {
        return (raining == player.getWorld().hasStorm());
    }
}
