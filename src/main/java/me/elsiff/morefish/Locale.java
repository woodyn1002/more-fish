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

        plugin.saveResource(langPath, false);
        plugin.saveResource(fishPath, false);

        File langFile = new File(plugin.getDataFolder(), langPath);
        this.lang = YamlConfiguration.loadConfiguration(langFile);

        File fishFile = new File(plugin.getDataFolder(), fishPath);
        this.fish = YamlConfiguration.loadConfiguration(fishFile);
    }

    public FileConfiguration getFishConfig() {
        return fish;
    }

    public String getString(String path) {
        String value = lang.getString(path);
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
