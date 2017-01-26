package me.elsiff.morefish;

import org.bukkit.ChatColor;

public class Rarity {
    private final String name;
    private final String displayName;
    private final boolean isDefault;
    private final double chance;
    private final ChatColor color;
    private final double additionalPrice;
    private final boolean noBroadcast;
    private final boolean noDisplay;
    private final boolean firework;

    public Rarity(Rarity rarity, double chance) {
        this(rarity.name, rarity.displayName, rarity.isDefault, chance, rarity.color, rarity.additionalPrice,
                rarity.noBroadcast, rarity.noDisplay, rarity.firework);
    }

    public Rarity(String name, String displayName, boolean isDefault, double chance, ChatColor color, double additionalPrice,
                  boolean noBroadcast, boolean noDisplay, boolean firework) {
        this.name = name;
        this.displayName = displayName;
        this.isDefault = isDefault;
        this.chance = chance;
        this.color = color;
        this.additionalPrice = additionalPrice;
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

    public boolean isDefault() {
        return isDefault;
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
