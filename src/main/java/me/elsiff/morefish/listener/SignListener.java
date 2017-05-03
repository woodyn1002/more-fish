package me.elsiff.morefish.listener;

import me.elsiff.morefish.MoreFish;
import org.bukkit.ChatColor;
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
        String firstLine = ChatColor.translateAlternateColorCodes('&', event.getLine(0))
                .replaceAll("§b", "");
        if ("[FishShop]".equalsIgnoreCase(firstLine)) {
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

            if ("§b[FishShop]".equalsIgnoreCase(sign.getLine(0))) {
                plugin.getFishShopGUI().openGUI(event.getPlayer());
            }
        }
    }
}
