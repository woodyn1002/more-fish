package me.elsiff.morefish;

import java.util.List;

public class CustomFish {
    private final String name;
    private final List<String> lore;
    private final double lengthMin;
    private final double lengthMax;
    private final String icon;
    private final List<String> commands;
    private final FishManager.Rarity rarity;

    public CustomFish(String name, List<String> lore, double lengthMin, double lengthMax, String icon, List<String> commands, FishManager.Rarity rarity) {
        this.name = name;
        this.lore = lore;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.icon = icon;
        this.commands = commands;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public double getLengthMin() {
        return lengthMin;
    }

    public double getLengthMax() {
        return lengthMax;
    }

    public String getIcon() {
        return icon;
    }

    public List<String> getCommands() {
        return commands;
    }

    public FishManager.Rarity getRarity() {
        return rarity;
    }
}
