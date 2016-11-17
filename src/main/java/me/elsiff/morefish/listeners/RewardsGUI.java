package me.elsiff.morefish.listeners;

import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.utils.ItemBuilder;
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
        Inventory inv = plugin.getServer().createInventory(player, (plugin.hasEconomy() ? 18 : 9), "Set the rewards of contest");

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

        inv.setItem(8, iconGuide);

        if (plugin.hasEconomy()) {
            double[] cashPrizes = plugin.getContestManager().getCashPrizes();

            for (int i = 0; i < 8; i ++) {
                double amount = cashPrizes[i];

                String target = ((i < 7) ? plugin.getOrdinal(i + 1) : "consolation");

                ItemStack iconEmerald = new ItemBuilder(Material.EMERALD)
                        .setDisplayName("§aPrize for " + target +": §2" + amount)
                        .addLore("§7Click to edit it.")
                        .build();

                inv.setItem(9 + i, iconEmerald);
            }

            ItemStack iconMoneyGuide = new ItemBuilder(Material.SIGN)
                    .setDisplayName("§bCash prizes")
                    .addLore("§7The second row is for",
                            "§7cash prizes.",
                            "§7Click the emeralds",
                            "§7to edit cash prizes.")
                    .build();

            inv.setItem(17, iconMoneyGuide);
        }

        player.openInventory(inv);
        users.add(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (users.contains(event.getWhoClicked().getUniqueId())) {
            if (event.getRawSlot() < 9 || event.getInventory().getSize() < event.getRawSlot()) {
                return;
            }

            event.setCancelled(true);

            if (9 <= event.getRawSlot() && event.getRawSlot() <= 16 && plugin.hasEconomy()) {
                editors.put(event.getWhoClicked().getUniqueId(), event.getRawSlot() - 9);

                event.getWhoClicked().closeInventory();

                event.getWhoClicked().sendMessage(plugin.prefix + "Enter the value with your chat.");
                event.getWhoClicked().sendMessage(plugin.prefix + "Or type 'cancel' to stop editing.");
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID id = event.getPlayer().getUniqueId();

        if (editors.containsKey(id)) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                editors.remove(id);
                openGUI(event.getPlayer());

                event.getPlayer().sendMessage(plugin.prefix + "Stopped editing mode.");
                return;
            }

            double value;
            try {
                value = Double.parseDouble(event.getMessage());
            } catch (NumberFormatException ex) {
                event.getPlayer().sendMessage(plugin.prefix + "'" + event.getMessage() + "' is not valid number. Please enter number again.");
                return;
            }

            if (value < 0) {
                event.getPlayer().sendMessage(plugin.prefix + "The number can't be negative. Please enter correct number.");
                return;
            }

            int index = editors.get(id);

            double[] cashPrizes = plugin.getContestManager().getCashPrizes();
            cashPrizes[index] = value;
            plugin.getContestManager().setCashPrizes(cashPrizes);

            editors.remove(id);
            openGUI(event.getPlayer());

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

            if (!editors.containsKey(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(plugin.prefix + "The changes have been saved.");
            }
        }
    }
}
