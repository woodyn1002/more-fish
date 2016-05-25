package me.elsiff.morefish.listeners;

import me.elsiff.morefish.MoreFish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private MoreFish plugin;

    public PlayerListener(MoreFish plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp() && plugin.getConfig().getBoolean("general.check-update") &&
                !plugin.getUpdateChecker().isUpToDate()) {
            event.getPlayer().sendMessage(plugin.prefix + "New version found: " + plugin.getUpdateChecker().getNewVersion());
            event.getPlayer().sendMessage(plugin.prefix + "https://www.spigotmc.org/resources/morefish.22926/");
        }
    }
}
