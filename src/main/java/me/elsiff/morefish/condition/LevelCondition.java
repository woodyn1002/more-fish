package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;

public class LevelCondition implements Condition {
    private int level;

    public LevelCondition(int level) {
        this.level = level;
    }

    @Override
    public boolean isSatisfying(Player player) {
        return (player.getLevel() >= level);
    }
}
