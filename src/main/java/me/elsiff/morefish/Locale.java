package me.elsiff.morefish;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Locale {
    private FileConfiguration lang;
    private FileConfiguration fish;

    public Locale(MoreFish plugin) {
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

        String msg = lang.getString("old-file");

        if (lang.getInt("version") != plugin.verLang) {
            plugin.getServer().getConsoleSender().sendMessage(String.format(msg, langPath));
        }

        if (fish.getInt("version") != plugin.verFish) {
            plugin.getServer().getConsoleSender().sendMessage(String.format(msg, fishPath));
        }
    }

    public FileConfiguration getFishConfig() {
        return fish;
    }

    public String getString(String path) {
        String value = lang.getString(path);
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public List<String> getStringList(String path) {
        List<String> list = new ArrayList<String>();

        for (String value : lang.getStringList(path)) {
            list.add(ChatColor.translateAlternateColorCodes('&', value));
        }

        return list;
    }
}
