package me.elsiff.morefish;

import me.elsiff.morefish.condition.Condition;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomFish {
    private final String internalName;
    private final String name;
    private final double lengthMin;
    private final double lengthMax;
    private final ItemStack icon;
    private final boolean skipItemFormat;
    private final List<String> commands;
    private final FoodEffects foodEffects;
    private final List<Condition> conditions;
    private final Rarity rarity;

    public CustomFish(String internalName, String name, double lengthMin, double lengthMax, ItemStack icon,
                      boolean skipItemFormat, List<String> commands, FoodEffects foodEffects, List<Condition> conditions, Rarity rarity) {
        this.internalName = internalName;
        this.name = name;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.icon = icon;
        this.skipItemFormat = skipItemFormat;
        this.commands = commands;
        this.foodEffects = foodEffects;
        this.conditions = conditions;
        this.rarity = rarity;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getName() {
        return name;
    }

    public double getLengthMin() {
        return lengthMin;
    }

    public double getLengthMax() {
        return lengthMax;
    }

    public ItemStack getIcon() {
        return icon.clone();
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

    public List<Condition> getConditions() {
        return conditions;
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
            return (saturation > 0);
        }

        public boolean hasCommands() {
            return (commands != null);
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public float getSaturation() {
            return saturation;
        }

        public void setSaturation(float saturation) {
            this.saturation = saturation;
        }

        public List<String> getCommands() {
            return commands;
        }

        public void setCommands(List<String> commands) {
            this.commands = commands;
        }
    }
}
