package me.elsiff.morefish;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Locale {
    private final MoreFish plugin;
    private FileConfiguration lang;
    private FileConfiguration fish;

    public Locale(MoreFish plugin) {
        this.plugin = plugin;
        String locale = plugin.getConfig().getString("general.locale");
        String langPath = "lang_" + locale + ".yml";
        String fishPath = "fish_" + locale + ".yml";

        File langFile = new File(plugin.getDataFolder(), langPath);
        File fishFile = new File(plugin.getDataFolder(), fishPath);

        if (!langFile.exists()) {
            plugin.saveResource(langPath, false);
        }

        if (!fishFile.exists()) {
            plugin.saveResource(fishPath, false);
        }

        this.lang = YamlConfiguration.loadConfiguration(langFile);
        this.fish = YamlConfiguration.loadConfiguration(fishFile);

        if (lang.getInt("version") != plugin.verLang) {
            plugin.getServer().getConsoleSender().sendMessage("§c[MoreFish] Your " + langPath + " is too old! Please make it up-to-date.");
        }

        if (fish.getInt("version") != plugin.verFish) {
            plugin.getServer().getConsoleSender().sendMessage("§c[MoreFish] Your " + fishPath + " is too old! Please make it up-to-date.");
        }
    }

    public FileConfiguration getFishConfig() {
        return fish;
    }

    public String getString(String path) {
        String value = lang.getString(path);
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
