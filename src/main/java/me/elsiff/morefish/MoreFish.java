package me.elsiff.morefish;

import me.elsiff.morefish.commands.GeneralCommands;
import me.elsiff.morefish.listeners.FishingListener;
import me.elsiff.morefish.listeners.PlayerListener;
import me.elsiff.morefish.listeners.RewardsGUI;
import me.elsiff.morefish.managers.BossBarManager;
import me.elsiff.morefish.managers.ContestManager;
import me.elsiff.morefish.managers.FishManager;
import me.elsiff.morefish.protocol.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class MoreFish extends JavaPlugin {
    public final int verConfig = 130;
    public final int verLang = 130;
    public final int verFish = 130;
    private Locale locale;
    private RewardsGUI rewardsGUI;
    private FishManager fishManager;
    private ContestManager contestManager;
    private BossBarManager bossBarManager;
    private UpdateChecker updateChecker;

    private Economy econ = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.locale = new Locale(this);

        if (getConfig().getInt("version") != verConfig) {
            getServer().getConsoleSender().sendMessage(String.format(getLocale().getString("old-file"), "config.yml"));
        }

        this.rewardsGUI = new RewardsGUI(this);
        this.fishManager = new FishManager(this);
        this.contestManager = new ContestManager(this);
        this.updateChecker = new UpdateChecker(this);

        // For 1.9+
        if (getConfig().getBoolean("general.use-boss-bar") && Material.getMaterial("SHIELD") != null) {
            this.bossBarManager = new BossBarManager(this);
        }

        getCommand("morefish").setExecutor(new GeneralCommands(this));

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new FishingListener(this), this);
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(rewardsGUI, this);

        if (setupEconomy()) {
            getLogger().info("Found Vault for economy support.");
        }

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (getConfig().getBoolean("auto-running.enable")) {
            final int required = getConfig().getInt("auto-running.required-players");
            final long timer = getConfig().getLong("auto-running.timer");
            long delay = getConfig().getLong("auto-running.delay");
            long period = getConfig().getLong("auto-running.period");

            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    if (getServer().getOnlinePlayers().size() >= required) {
                        getServer().dispatchCommand(getServer().getConsoleSender(), "morefish start " + timer);
                    }
                }
            }, delay * 20, period * 20);
        }

        getLogger().info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin has been disabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public Locale getLocale() {
        return locale;
    }

    public FishManager getFishManager() {
        return fishManager;
    }

    public ContestManager getContestManager() {
        return contestManager;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public boolean hasBossBar() {
        return (getBossBarManager() != null);
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public String getOrdinal(int number) {
        switch (number) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                if (number > 20) {
                    return (number / 10) + getOrdinal(number % 10);
                } else {
                    return number + "th";
                }
        }
    }

    public String getTimeString(long sec) {
        StringBuilder builder = new StringBuilder();

        int minutes = (int) (sec / 60);
        int second = (int) (sec - minutes * 60);

        if (minutes > 0) {
            builder.append(minutes);
            builder.append(getLocale().getString("time-format-minutes"));
            builder.append(" ");
        }

        builder.append(second);
        builder.append(getLocale().getString("time-format-seconds"));

        return builder.toString();
    }

    public RewardsGUI getRewardsGUI() {
        return rewardsGUI;
    }

    public boolean hasEconomy() {
        return (econ != null);
    }

    public Economy getEconomy() {
        return econ;
    }

    public void reloadLocale() {
        locale = new Locale(this);
    }
}
