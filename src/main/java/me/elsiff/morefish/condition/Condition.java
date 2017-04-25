package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;

public interface Condition {
    boolean isSatisfying(Player player);
}
