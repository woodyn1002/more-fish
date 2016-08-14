package me.elsiff.morefish.listeners;

import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardsGUI implements Listener {
    private final MoreFish plugin;
    private final Set<UUID> users = new HashSet<UUID>();
    private final Map<UUID, Integer> editors = new HashMap<UUID, Integer>();

    public RewardsGUI(MoreFish plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = plugin.getServer().createInventory(player, 18, "Set the rewards of contest");

        ItemStack[] rewards = plugin.getContestManager().getRewards();

        for (int i = 0; i < 8 && i < rewards.length; i ++) {
            inv.setItem(i, rewards[i]);
        }

        ItemStack iconGuide = new ItemBuilder(Material.SIGN)
                .setDisplayName("§bPut your items to the slots!")
                .addLore("§7The first slot means",
                        "§7the reward of 1st,",
                        "§7and the second one means",
                        "§7the reward of 2nd.",
                        "§78th slot is for consolation.",
                        "§7Empty slots mean no reward.")
                .build();

        ItemStack iconMoneyGuide = new ItemBuilder(Material.SIGN)
                .setDisplayName("§bCash prizes")
                .addLore("§7The second row is for",
                        "§7cash prizes.",
                        "§7Click the emeralds",
                        "§7to edit cash prizes.")
                .build();

        inv.setItem(8, iconGuide);
        inv.setItem(17, iconMoneyGuide);

        for (int i = 0; i < 7; i ++) {
            double amount = 0;

            ItemStack iconEmerald = new ItemBuilder(Material.EMERALD)
                    .setDisplayName("§aAmount: " + amount)
                    .addLore("§7Click to edit it.")
                    .build();
        }

        player.openInventory(inv);
        users.add(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (users.contains(event.getWhoClicked().getUniqueId())) {
            if (event.getRawSlot() < 9) {
                return;
            }

            event.setCancelled(true);

            if (9 < event.getRawSlot() && event.getRawSlot() < 17) {
                event.getWhoClicked().closeInventory();

                event.getWhoClicked().sendMessage(plugin.prefix + "Enter the value with your chat.");

                editors.put(event.getWhoClicked().getUniqueId(), event.getRawSlot() - 9);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID id = event.getPlayer().getUniqueId();

        if (editors.containsKey(id)) {

            double value;
            try {
                value = Double.parseDouble(event.getMessage());
            } catch (NumberFormatException ex) {
                event.getPlayer().sendMessage(plugin.prefix + "'" + event.getMessage() + "' is not valid number. Please enter the number again.");
                return;
            }

            int index = editors.get(id);

            double[] cashPrizes = plugin.getContestManager().getCashPrizes();
            cashPrizes[index] = value;
            plugin.getContestManager().setCashPrizes(cashPrizes);

            event.getPlayer().sendMessage(plugin.prefix + "The value have been entered.");

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
