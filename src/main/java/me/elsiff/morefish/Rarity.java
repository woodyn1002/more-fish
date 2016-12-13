package me.elsiff.morefish;

import org.bukkit.ChatColor;

public class Rarity {
    private String name;
    private String displayName;
    private double chance;
    private ChatColor color;
    private double additionalPrice;
    private boolean noBroadcast;
    private boolean noDisplay;
    private boolean firework;

    public Rarity(String name, String displayName, double chance, ChatColor color, double additionalPrice,
                  boolean noBroadcast, boolean noDisplay, boolean firework) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.color = color;
        this.noBroadcast = noBroadcast;
        this.noDisplay = noDisplay;
        this.firework = firework;
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

    public double getAdditionalPrice() {
        return additionalPrice;
    }

    public boolean isNoBroadcast() {
        return noBroadcast;
    }

    public boolean isNoDisplay() {
        return noDisplay;
    }

    public boolean hasFirework() {
        return firework;
    }
}
