package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;

public class TimeCondition implements Condition {
    private String time;

    public TimeCondition(String time) {
        this.time = time;
    }

    @Override
    public boolean isSatisfying(Player player) {
        long tick = player.getWorld().getTime() % 24000;
        switch (time) {
            case "day":
                return (1000 <= tick && tick < 13000);
            case "night":
                return (13000 <= tick && tick < 1000);
            default:
                return false;
        }
    }
}
