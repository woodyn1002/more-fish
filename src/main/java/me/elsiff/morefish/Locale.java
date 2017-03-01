package me.elsiff.morefish;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Locale {
    private final MoreFish plugin;
    private final File folder;
    private final String langPath;
    private final String fishPath;
    private FileConfiguration lang;
    private FileConfiguration fish;

    public Locale(MoreFish plugin) {
        this.plugin = plugin;

        String locale = plugin.getConfig().getString("general.locale");
        folder = new File(plugin.getDataFolder(), "locale");
        langPath = "lang_" + locale + ".yml";
        fishPath = "fish_" + locale + ".yml";

        loadFiles();

        String msg = lang.getString("old-file");

        if (lang.getInt("version") != plugin.verLang) {
            plugin.getServer().getConsoleSender().sendMessage(String.format(msg, langPath));
        }

        if (fish.getInt("version") != plugin.verFish) {
            plugin.getServer().getConsoleSender().sendMessage(String.format(msg, fishPath));
        }
    }

    private FileConfiguration loadConfiguration(File folder, String path) {
        File langFile = new File(folder, path);

        if (!langFile.exists()) {
            plugin.saveResource("locale\\" + path, false);
        }

        return YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration getFishConfig() {
        return fish;
    }

    public boolean loadFiles() {
        try {
            this.lang = loadConfiguration(folder, langPath);
            this.fish = loadConfiguration(folder, fishPath);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getString(String path) {
        String value = lang.getString(path);
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public List<String> getStringList(String path) {
        List<String> list = new ArrayList<>();

        for (String value : lang.getStringList(path)) {
            list.add(ChatColor.translateAlternateColorCodes('&', value));
        }

        return list;
    }
}
