package me.elsiff.morefish;

import me.elsiff.morefish.utils.IdentityUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class FishManager {
    private final MoreFish plugin;
    private final Random random = new Random();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
    private final List<Rarity> rarityList = new ArrayList<Rarity>();
    private final Map<String, CustomFish> fishMap = new HashMap<String, CustomFish>();
    private final Map<Rarity, List<CustomFish>> rarityMap = new HashMap<Rarity, List<CustomFish>>();
    private final Map<String, String> nameMap = new HashMap<String, String>();

    public FishManager(MoreFish plugin) {
        this.plugin = plugin;

        loadFishList();
    }

    public void loadFishList() {
        fishMap.clear();
        rarityMap.clear();

        FileConfiguration config = plugin.getLocale().getFishConfig();
        ConfigurationSection rarities = config.getConfigurationSection("rarity-list");

        for (String path : rarities.getKeys(false)) {
            String displayName = rarities.getString(path + ".display-name");
            double chance = rarities.getDouble(path + ".chance") * 0.01;
            ChatColor color = ChatColor.valueOf(rarities.getString(path + ".color").toUpperCase());

            boolean noBroadcast = ((rarities.contains(path + ".no-broadcast")) && rarities.getBoolean(path + ".no-broadcast"));
            boolean noDisplay = ((rarities.contains(path + ".no-display")) && rarities.getBoolean(path + ".no-display"));

            Rarity rarity = new Rarity(path, displayName, chance, color, noBroadcast, noDisplay);

            rarityList.add(rarity);
        }

        for (Rarity key : rarityList) {
            rarityMap.put(key, new ArrayList<CustomFish>());
        }

        for (Rarity rarity : rarityList) {
            ConfigurationSection section = config.getConfigurationSection("fish-list." + rarity.getName().toLowerCase());

            for (String path : section.getKeys(false)) {
                String displayName = section.getString(path + ".display-name");
                List<String> lore = new ArrayList<String>();
                double lengthMin = section.getDouble(path + ".length-min");
                double lengthMax = section.getDouble(path + ".length-max");
                String icon = section.getString(path + ".icon");
                List<String> commands = new ArrayList<String>();
                CustomFish.FoodEffects foodEffects = new CustomFish.FoodEffects();

                if (section.contains(path + ".lore")) {
                    lore = section.getStringList(path + ".lore");
                }

                if (section.contains(path + ".commands")) {
                    commands = section.getStringList(path + ".commands");
                }

                if (section.contains(path + ".food-effects")) {
                    if (section.contains(path + ".food-effects.points")) {
                        foodEffects.setPoints(section.getInt(path + ".food-effects.points"));
                    }

                    if (section.contains(path + ".food-effects.saturation")) {
                        foodEffects.setSaturation((float) section.getDouble(path + ".food-effects.saturation"));
                    }

                    if (section.contains(path + ".food-effects.commands")) {
                        foodEffects.setCommands(section.getStringList(path + ".food-effects.commands"));
                    }
                }

                CustomFish fish = new CustomFish(displayName, lore, lengthMin, lengthMax, icon, commands, foodEffects, rarity);

                this.fishMap.put(path, fish);
                this.rarityMap.get(rarity).add(fish);
                this.nameMap.put(displayName, path);
            }
        }

        plugin.getLogger().info("Loaded " + fishMap.size() + " fish successfully.");
    }

    public CaughtFish generateRandomFish() {
        Rarity rarity = getRandomRarity();
        CustomFish type = getRandomFish(rarity);

        return createCaughtFish(type);
    }

    public CustomFish getCustomFish(String name) {
        return fishMap.get(name);
    }

    public String getFishName(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return null;
        }

        String displayName = item.getItemMeta().getDisplayName().substring(2);

        return nameMap.get(displayName);
    }

    public ItemStack getItemStack(CaughtFish fish, String fisher) {
        String[] split = fish.getIcon().split("\\|");
        Material material = IdentityUtils.getMaterial(split[0]);
        short durability = ((split.length > 1) ? Short.parseShort(split[1]) : 0);

        if (material == null) {
            plugin.getLogger().warning("'" + split[0] + "' is invalid item id!");
            return null;
        }

        ItemStack itemStack = new ItemStack(material, 1, durability);
        ItemMeta meta = itemStack.getItemMeta();

        FileConfiguration config = plugin.getLocale().getFishConfig();

        String displayName = config.getString("item-format.display-name")
                .replaceAll("%player%", fisher)
                .replaceAll("%rarity%", fish.getRarity().getDisplayName())
                .replaceAll("%raritycolor%", fish.getRarity().getColor() + "")
                .replaceAll("%fish%", fish.getName());
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);

        List<String> lore = new ArrayList<String>();

        for (String str : config.getStringList("item-format.lore")) {
            String line = str
                    .replaceAll("%player%", fisher)
                    .replaceAll("%rarity%", fish.getRarity().getDisplayName())
                    .replaceAll("%raritycolor%", fish.getRarity().getColor() + "")
                    .replaceAll("%length%", fish.getLength() + "")
                    .replaceAll("%fish%", fish.getName())
                    .replaceAll("%date%", dateFormat.format(new Date()));

            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.add(line);
        }

        if (!fish.getLore().isEmpty()) {
            for (String line : fish.getLore()) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private CaughtFish createCaughtFish(CustomFish fish) {
        double length;

        if (fish.getLengthMax() == fish.getLengthMin()) {
            length = fish.getLengthMax();
        } else {
            int min = (int) fish.getLengthMin();
            int max = (int) fish.getLengthMax();

            length = random.nextInt(max - min + 1) + min;
            length += 0.1 * random.nextInt(10);
        }

        return new CaughtFish(fish, length);
    }

    private Rarity getRandomRarity() {
        double currentVar = 0.0D;
        double randomVar = Math.random();

        for (Rarity rarity : rarityList) {
            currentVar += rarity.getChance();

            if (randomVar <= currentVar) {
                return rarity;
            }
        }

        return null;
    }

    private CustomFish getRandomFish(Rarity rarity) {
        List<CustomFish> list = rarityMap.get(rarity);
        int index = random.nextInt(list.size());

        return list.get(index);
    }
}
