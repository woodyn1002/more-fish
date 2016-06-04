package me.elsiff.morefish;

import org.bukkit.ChatColor;

public class Rarity {
    private String name;
    private String displayName;
    private double chance;
    private ChatColor color;
    private boolean noBroadcast;
    private boolean noDisplay;

    public Rarity(String name, String displayName, double chance, ChatColor color, boolean noBroadcast, boolean noDisplay) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.color = color;
        this.noBroadcast = noBroadcast;
        this.noDisplay = noDisplay;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getChance() {
        return chance;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isNoBroadcast() {
        return noBroadcast;
    }

    public boolean isNoDisplay() {
        return noDisplay;
    }
}
