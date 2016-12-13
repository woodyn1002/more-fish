package me.elsiff.morefish.listeners;

import me.elsiff.morefish.MoreFish;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    private MoreFish plugin;

    public SignListener(MoreFish plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).replaceAll("&b", "").replaceAll("§b", "").equalsIgnoreCase("[FishShop]")) {
            if (!event.getPlayer().hasPermission("morefish.admin")) {
                event.getPlayer().sendMessage(plugin.getLocale().getString("no-permission"));
                return;
            }

            event.setLine(0, "§b[FishShop]");
            event.getPlayer().sendMessage(plugin.getLocale().getString("created-sign-shop"));
        }
    }

    @EventHandler
    public void onInteracat(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign &&
                plugin.getFishShopGUI() != null) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            if (sign.getLine(0).equalsIgnoreCase("§b[FishShop]")) {
                plugin.getFishShopGUI().openGUI(event.getPlayer());
            }
        }
    }
}
