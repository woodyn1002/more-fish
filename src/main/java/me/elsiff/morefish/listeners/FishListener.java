package me.elsiff.morefish.listeners;

import me.elsiff.morefish.CaughtFish;
import me.elsiff.morefish.ContestManager;
import me.elsiff.morefish.FishManager;
import me.elsiff.morefish.MoreFish;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishListener implements Listener {
    private final MoreFish plugin;
    private final ContestManager contest;

    public FishListener(MoreFish plugin) {
        this.plugin = plugin;
        this.contest = plugin.getContestManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (plugin.getConfig().getBoolean("general.only-for-contest") && !contest.hasStarted()) {
                return;
            }

            CaughtFish fish = plugin.getFishManager().generateRandomFish();


            String msgFish = getMessage("catch-fish.text", event.getPlayer(), fish);
            int ancFish = plugin.getConfig().getInt("messages.catch-fish.announce");

            announceMessage(event.getPlayer(), msgFish, ancFish);


            if (!fish.getCommands().isEmpty()) {
                executeCommands(event.getPlayer(), fish);
            }


            if (contest.hasStarted()) {
                if (contest.isNew1st(fish)) {
                    String msgContest = getMessage("get-1st.text", event.getPlayer(), fish);
                    int ancContest = plugin.getConfig().getInt("messages.get-1st.announce");

                    announceMessage(event.getPlayer(), msgContest, ancContest);
                }

                contest.addRecord(event.getPlayer(), fish);
            }


            ItemStack itemStack = plugin.getFishManager().getItemStack(fish, event.getPlayer().getName());
            Item caught = (Item) event.getCaught();
            caught.setItemStack(itemStack);
        }
    }

    private String getMessage(String path, Player player, CaughtFish fish) {
        String message = plugin.getConfig().getString("messages." + path);

        message = message.replaceAll("%player%", player.getName())
                .replaceAll("%length%", fish.getLength() + "")
                .replaceAll("%rarity%", fish.getRarity().name())
                .replaceAll("%raritycolor%", fish.getRarity().getColor() + "")
                .replaceAll("%fish%", fish.getName())
                .replaceAll("%fishwithrarity%", (((fish.getRarity() == FishManager.Rarity.COMMON) ? "" : fish.getRarity().name() + " ")) + fish.getName());

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    private void announceMessage(Player player, String message, int announce) {
        if (message.length() == 0)
            return;

        switch (announce) {
            case -1:
                plugin.getServer().broadcastMessage(message);
                break;
            case 0:
                player.sendMessage(message);
                break;
            default:
                Location loc = player.getLocation();

                for (Player other : player.getWorld().getPlayers()) {
                    if (other.getLocation().distance(loc) <= announce) {
                        other.sendMessage(message);
                    }
                }
        }
    }

    private void executeCommands(Player player, CaughtFish fish) {
        for (String command : fish.getCommands()) {
            String str = command.replaceAll("@p", player.getName())
                    .replaceAll("%fish%", fish.getName())
                    .replaceAll("%length%", fish.getLength() + "");

            str = ChatColor.translateAlternateColorCodes('&', str);

            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), str);
        }
    }
}
