package me.elsiff.morefish;

import java.util.List;

public class CustomFish {
    private final String name;
    private final List<String> lore;
    private final double lengthMin;
    private final double lengthMax;
    private final String icon;
    private final boolean skipItemFormat;
    private final List<String> commands;
    private final FoodEffects foodEffects;
    private final Rarity rarity;

    public CustomFish(String name, List<String> lore, double lengthMin, double lengthMax, String icon, boolean skipItemFormat, List<String> commands, FoodEffects foodEffects, Rarity rarity) {
        this.name = name;
        this.lore = lore;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.icon = icon;
        this.skipItemFormat = skipItemFormat;
        this.commands = commands;
        this.foodEffects = foodEffects;
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

    public boolean hasNoItemFormat() {
        return skipItemFormat;
    }

    public List<String> getCommands() {
        return commands;
    }

    public FoodEffects getFoodEffects() {
        return foodEffects;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public static class FoodEffects {
        private int points;
        private float saturation;
        private List<String> commands;

        public FoodEffects() {
            this.points = -1;
            this.saturation = -1.0F;
            this.commands = null;
        }

        public boolean hasPoints() {
            return (points != -1);
        }

        public boolean hasSaturation() {
            return (saturation != -1.0F);
        }

        public boolean hasCommands() {
            return (commands != null);
        }

        public int getPoints() {
            return points;
        }

        public float getSaturation() {
            return saturation;
        }

        public List<String> getCommands() {
            return commands;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public void setSaturation(float saturation) {
            this.saturation = saturation;
        }

        public void setCommands(List<String> commands) {
            this.commands = commands;
        }
    }
}
