package me.elsiff.morefish.listeners;

import me.elsiff.morefish.CaughtFish;
import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FishShopGUI implements Listener {
    private final MoreFish plugin;
    private final Set<UUID> users = new HashSet<>();

    public FishShopGUI(MoreFish plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        String title = plugin.getLocale().getString("shop-gui-title");
        Inventory inv = plugin.getServer().createInventory(player, 36, title);

        ItemStack iconGlass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).setDisplayName("§r").build();
        for (int i = 0; i < 9; i ++) {
            inv.setItem(27 + i, iconGlass);
        }

        updateEmeraldIcon(inv, false);

        player.openInventory(inv);
        users.add(player.getUniqueId());
    }

    private void updateEmeraldIcon(Inventory inv, boolean calculating) {
        String displayName;

        if (calculating) {
            displayName = plugin.getLocale().getString("shop-emerald-icon-calculating");
        } else {
            double price = getTotalPrice(inv);
            String priceStr = getPriceString(price);
            displayName = plugin.getLocale().getString("shop-emerald-icon-name")
                    .replaceAll("%price%", priceStr);
        }

        ItemStack iconEmerald = new ItemBuilder(Material.EMERALD)
                .setDisplayName(displayName)
                .build();
        inv.setItem(31, iconEmerald);
    }

    private double getTotalPrice(Inventory inv) {
        double total = 0.0D;

        for (int i = 0; i < 27; i ++) {
            ItemStack itemStack = inv.getItem(i);

            if (itemStack == null || itemStack.getType() == Material.AIR ||
                    !plugin.getFishManager().isCustomFish(itemStack)) {
                continue;
            }

            CaughtFish fish = plugin.getFishManager().getCaughtFish(itemStack);
            double multiplier = plugin.getConfig().getDouble("fish-shop.multiplier");
            double additionalPrice = fish.getRarity().getAdditionalPrice();
            double price = (fish.getLength() * multiplier) + additionalPrice;

            if (price < 0) {
                price = 0.0D;
            }

            if (plugin.getConfig().getBoolean("fish-shop.round-demical-points")) {
                price = (int) price;
            }

            total += price * itemStack.getAmount();
        }

        return total;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (users.contains(event.getWhoClicked().getUniqueId())) {
            event.getInventory().setItem(31, new ItemStack(Material.AIR));
            final Inventory inv = event.getInventory();
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    updateEmeraldIcon(inv, false);
                }
            }, 1L);

            if (27 <= event.getRawSlot() && event.getRawSlot() <= 35) {
                event.setCancelled(true);

                if (event.getRawSlot() == 31) {
                    Player player = (Player) event.getWhoClicked();
                    double price = getTotalPrice(event.getInventory());

                    if (!plugin.hasEconomy() || !plugin.getVaultHooker().getEconomy().hasAccount(player)) {
                        return;
                    }

                    boolean sold = false;
                    for (int i = 0; i < 27; i ++) {
                        ItemStack itemStack = event.getInventory().getItem(i);

                        if (itemStack == null || itemStack.getType() == Material.AIR ||
                                !plugin.getFishManager().isCustomFish(itemStack)) {
                            continue;
                        }

                        event.getInventory().setItem(i, new ItemStack(Material.AIR));
                        sold = true;
                    }

                    if (!sold) {
                        player.sendMessage(plugin.getLocale().getString("shop-no-fish"));
                        return;
                    }

                    plugin.getVaultHooker().getEconomy().depositPlayer(player, price);

                    String priceStr = getPriceString(price);
                    player.sendMessage(plugin.getLocale().getString("shop-sold")
                            .replaceAll("%price%", priceStr + ""));
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (users.contains(event.getWhoClicked().getUniqueId())) {
            event.getInventory().setItem(31, new ItemStack(Material.AIR));
            final Inventory inv = event.getInventory();
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    updateEmeraldIcon(inv, false);
                }
            }, 1L);
        }
    }

    private String getPriceString(double price) {
        int intPrice = (int) price;
        return ((price == intPrice) ? intPrice + "" : price + "");
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (users.contains(event.getPlayer().getUniqueId())) {
            users.remove(event.getPlayer().getUniqueId());

            for (int i = 0; i < 27; i ++) {
                ItemStack itemStack = event.getInventory().getItem(i);

                if (itemStack == null || itemStack.getType() == Material.AIR ||
                        !plugin.getFishManager().isCustomFish(itemStack)) {
                    continue;
                }

                if (event.getPlayer().getInventory().firstEmpty() == -1) {
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                } else {
                    event.getPlayer().getInventory().addItem(itemStack);
                }
            }
        }
    }
}
