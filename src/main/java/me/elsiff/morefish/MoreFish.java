package me.elsiff.morefish;

import me.elsiff.morefish.commands.GeneralCommands;
import me.elsiff.morefish.listeners.FishListener;
import me.elsiff.morefish.listeners.PlayerListener;
import me.elsiff.morefish.listeners.RewardsGUI;
import me.elsiff.morefish.protocol.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreFish extends JavaPlugin {
    public final String prefix = "§b[MoreFish]§r ";
    private RewardsGUI rewardsGUI;
    private FishManager fishManager;
    private ContestManager contestManager;
    private UpdateChecker updateChecker;

    private Economy econ = null;

    @Override
    public void onEnable() {
        int configVer = 112;

        saveDefaultConfig();

        if (getConfig().getInt("version") != configVer) {
            getServer().getConsoleSender().sendMessage("§c[MoreFish] Your config.yml is too old! Please make it up-to-date.");
        }

        this.rewardsGUI = new RewardsGUI(this);
        this.fishManager = new FishManager(this);
        this.contestManager = new ContestManager(this);
        this.updateChecker = new UpdateChecker(this);

        getCommand("morefish").setExecutor(new GeneralCommands(this));

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new FishListener(this), this);
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(rewardsGUI, this);

        if (setupEconomy()) {
            getLogger().info("Found Vault for economy support.");
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

    public FishManager getFishManager() {
        return fishManager;
    }

    public ContestManager getContestManager() {
        return contestManager;
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
                return number + "th";
        }
    }

    public RewardsGUI getRewardsGUI() {
        return rewardsGUI;
    }

    public Economy getEconomy() {
        return econ;
    }
}
