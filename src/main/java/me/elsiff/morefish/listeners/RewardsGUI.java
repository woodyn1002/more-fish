package me.elsiff.morefish.listeners;

import me.elsiff.morefish.MoreFish;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RewardsGUI implements Listener {
    private final MoreFish plugin;
    private final Set<UUID> users = new HashSet<UUID>();

    public RewardsGUI(MoreFish plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = plugin.getServer().createInventory(player, 9, "Set the rewards of contest");

        ItemStack[] rewards = plugin.getContestManager().getRewards();

        for (int i = 0; i < 8 && i < rewards.length; i ++) {
            inv.setItem(i, rewards[i]);
        }

        ItemStack icon = new ItemStack(Material.SIGN);
        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName("§bPut your items to the slots!");

        List<String> lore = new ArrayList<String>();
        lore.add("§7The first slot means");
        lore.add("§7the reward of 1st,");
        lore.add("§7and the second one means");
        lore.add("§7the reward of 2nd.");
        lore.add("§7Empty slots mean no reward.");

        meta.setLore(lore);

        icon.setItemMeta(meta);
        inv.setItem(8, icon);

        player.openInventory(inv);
        users.add(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (users.contains(event.getWhoClicked().getUniqueId()) && event.getRawSlot() == 8) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (users.contains(event.getPlayer().getUniqueId())) {
            users.remove(event.getPlayer().getUniqueId());

            ItemStack[] rewards = new ItemStack[8];

            for (int i = 0; i < 8; i ++) {
                ItemStack stack = event.getInventory().getItem(i);

                if (stack == null || stack.getType() == Material.AIR)
                    continue;

                rewards[i] = stack;
            }

            plugin.getContestManager().setRewards(rewards);

            event.getPlayer().sendMessage(plugin.prefix + "The changes have been saved.");
        }
    }
}
