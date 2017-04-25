package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;

public class ThunderingCondition implements Condition {
    public boolean thundering;

    public ThunderingCondition(boolean thundering) {
        this.thundering = thundering;
    }

    @Override
    public boolean isSatisfying(Player player) {
        return (thundering == player.getWorld().isThundering());
    }
}
