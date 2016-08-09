package me.elsiff.morefish.listeners;

import me.elsiff.morefish.CustomFish;
import me.elsiff.morefish.MoreFish;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        String name = plugin.getFishManager().getFishName(event.getItem());

        if (name != null) {
            CustomFish fish = plugin.getFishManager().getCustomFish(name);
            CustomFish.FoodEffects effects = fish.getFoodEffects();

            boolean cancel = false;

            if (effects.hasPoints()) {
                cancel = true;

                int foodLevel = event.getPlayer().getFoodLevel() + effects.getPoints();

                if (foodLevel < 0)
                    foodLevel = 0;
                if (foodLevel > 20)
                    foodLevel = 20;

                event.getPlayer().setFoodLevel(foodLevel);
            }

            if (effects.hasSaturation()) {
                cancel = true;

                event.getPlayer().setSaturation(effects.getSaturation());
            }

            if (effects.hasCommands()) {
                for (String command : effects.getCommands()) {
                    String str = command.replaceAll("@p", event.getPlayer().getName());

                    str = ChatColor.translateAlternateColorCodes('&', str);

                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), str);
                }
            }


            if (cancel) {
                event.setCancelled(true);

                int amount = event.getItem().getAmount();

                if (amount == 1) {
                    event.getPlayer().getInventory().remove(event.getItem());
                } else {
                    event.getItem().setAmount(amount - 1);
                }
            }
        }
    }
}
