package me.elsiff.morefish;

import me.elsiff.morefish.commands.GeneralCommands;
import me.elsiff.morefish.hookers.CitizensHooker;
import me.elsiff.morefish.hookers.VaultHooker;
import me.elsiff.morefish.listeners.*;
import me.elsiff.morefish.managers.BossBarManager;
import me.elsiff.morefish.managers.ContestManager;
import me.elsiff.morefish.managers.FishManager;
import me.elsiff.morefish.protocol.UpdateChecker;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class MoreFish extends JavaPlugin {
    private static MoreFish instance;
    public final int verConfig = 200;
    public final int verLang = 200;
    public final int verFish = 200;
    private Locale locale;
    private RewardsGUI rewardsGUI;
    private FishShopGUI fishShopGUI;
    private FishManager fishManager;
    private ContestManager contestManager;
    private BossBarManager bossBarManager;
    private UpdateChecker updateChecker;

    private VaultHooker vaultHooker;
    private CitizensHooker citizensHooker;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.locale = new Locale(this);

        if (getConfig().getInt("version") != verConfig) {
            getServer().getConsoleSender().sendMessage(String.format(getLocale().getString("old-file"), "config.yml"));
        }

        this.rewardsGUI = new RewardsGUI(this);
        this.fishShopGUI = new FishShopGUI(this);
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

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            vaultHooker = new VaultHooker(this);

            if (vaultHooker.setupEconomy()) {
                getLogger().info("Found Vault for economy support.");
            } else {
                vaultHooker = null;
            }
        }

        if (getServer().getPluginManager().getPlugin("Citizens") != null && getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            citizensHooker = new CitizensHooker(this);
            citizensHooker.registerTrait();
            getLogger().info("Found Citizens for Fish Shop Trait.");
        }

        if (hasEconomy() && getConfig().getBoolean("fish-shop.enable")) {
            manager.registerEvents(new SignListener(this), this);
            manager.registerEvents(fishShopGUI, this);
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
        if (getCitizensHooker() != null) {
            getCitizensHooker().deregisterTrait();
        }

        getLogger().info("Plugin has been disabled!");
    }

    public static MoreFish getInstance() {
        return instance;
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

    public FishShopGUI getFishShopGUI() {
        return fishShopGUI;
    }

    public boolean hasEconomy() {
        return (vaultHooker != null);
    }

    public VaultHooker getVaultHooker() {
        return vaultHooker;
    }

    public CitizensHooker getCitizensHooker() {
        return citizensHooker;
    }

    public void reloadLocale() {
        locale = new Locale(this);
    }
}
