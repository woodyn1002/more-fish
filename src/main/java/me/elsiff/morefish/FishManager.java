package me.elsiff.morefish;

import me.elsiff.morefish.util.IdentityUtils;
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

    public FishManager(MoreFish plugin) {
        this.plugin = plugin;

        loadFishList();
    }

    public void loadFishList() {
        fishMap.clear();
        rarityMap.clear();

        FileConfiguration config = plugin.getConfig();

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

                if (section.contains(path + ".lore")) {
                    lore = section.getStringList(path + ".lore");
                }

                if (section.contains(path + ".commands")) {
                    commands = section.getStringList(path + ".commands");
                }

                CustomFish fish = new CustomFish(displayName, lore, lengthMin, lengthMax, icon, commands, rarity);

                this.fishMap.put(path, fish);
                this.rarityMap.get(rarity).add(fish);
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

        String displayName = plugin.getConfig().getString("item-format.display-name")
                .replaceAll("%player%", fisher)
                .replaceAll("%rarity%", fish.getRarity().getDisplayName())
                .replaceAll("%raritycolor%", fish.getRarity().getColor() + "")
                .replaceAll("%fish%", fish.getName());
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);

        List<String> lore = new ArrayList<String>();

        for (String str : plugin.getConfig().getStringList("item-format.lore")) {
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
