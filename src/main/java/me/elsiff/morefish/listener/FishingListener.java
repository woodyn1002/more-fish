package me.elsiff.morefish.listener;

import me.elsiff.morefish.CaughtFish;
import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.event.PlayerCatchCustomFishEvent;
import me.elsiff.morefish.manager.ContestManager;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashSet;
import java.util.Set;

public class FishingListener implements Listener {
    private final MoreFish plugin;
    private final ContestManager contest;

    public FishingListener(MoreFish plugin) {
        this.plugin = plugin;
        this.contest = plugin.getContestManager();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
            if (!contest.hasStarted() && plugin.getConfig().getBoolean("general.no-fishing-unless-contest")) {
                event.setCancelled(true);

                String msg = plugin.getLocale().getString("no-fishing-allowed");
                event.getPlayer().sendMessage(msg);
                return;
            }
            if (!isFishingEnabled(event)) {
                return;
            }

            executeFishingActions(event.getPlayer(), event);
        }
    }

    private boolean isFishingEnabled(PlayerFishEvent event) {
        // Check if the world hasn't disabled
        if (plugin.getConfig().getStringList("general.contest-disabled-worlds")
                .contains(event.getPlayer().getWorld().getName()))
            return false;

        // Check if the contest is ongoing
        if (plugin.getConfig().getBoolean("general.only-for-contest") &&
                !contest.hasStarted())
            return false;

        // Check if the caught is fish
        return (!plugin.getConfig().getBoolean("general.replace-only-fish") ||
                ((Item) event.getCaught()).getItemStack().getType() == Material.RAW_FISH);
    }

    private void executeFishingActions(Player catcher, PlayerFishEvent event) {
        CaughtFish fish = plugin.getFishManager().generateRandomFish(catcher);

        PlayerCatchCustomFishEvent customEvent = new PlayerCatchCustomFishEvent(catcher, fish, event);
        plugin.getServer().getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            return;
        }

        boolean new1st = contest.hasStarted() && contest.isNew1st(fish);
        announceMessages(catcher, fish, new1st);

        if (fish.getRarity().hasFirework())
            launchFirework(catcher.getLocation().add(0, 1, 0));
        if (!fish.getCommands().isEmpty())
            executeCommands(catcher, fish);

        if (new1st) {
            contest.addRecord(catcher, fish);
        }

        ItemStack itemStack = plugin.getFishManager().getItemStack(fish, event.getPlayer().getName());
        Item caught = (Item) event.getCaught();
        caught.setItemStack(itemStack);
    }

    private void announceMessages(Player catcher, CaughtFish fish, boolean new1st) {
        String msgFish = getMessage("catch-fish", catcher, fish);
        String msgContest = getMessage("get-1st", catcher, fish);
        int ancFish = plugin.getConfig().getInt("messages.announce-catch");
        int ancContest = plugin.getConfig().getInt("messages.announce-new-1st");

        if (fish.getRarity().isNoBroadcast())
            ancFish = 0;
        if (new1st)
            ancFish = ancContest;

        getMessageReceivers(ancFish, catcher)
                .forEach(player -> player.sendMessage(msgFish));

        if (new1st) {
            getMessageReceivers(ancContest, catcher)
                    .forEach(player -> player.sendMessage(msgContest));
        }
    }

    private String getMessage(String path, Player player, CaughtFish fish) {
        String message = plugin.getLocale().getString(path);

        message = message.replaceAll("%player%", player.getName())
                .replaceAll("%length%", fish.getLength() + "")
                .replaceAll("%rarity%", fish.getRarity().getDisplayName())
                .replaceAll("%rarity_color%", fish.getRarity().getColor() + "")
                .replaceAll("%fish%", fish.getName())
                .replaceAll("%fish_with_rarity%", ((fish.getRarity().isNoDisplay()) ? "" : fish.getRarity().getDisplayName() + " ") + fish.getName());

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    private Set<Player> getMessageReceivers(int announceValue, Player catcher) {
        Set<Player> players = new HashSet<>();

        switch (announceValue) {
            case -1:
                players.addAll(plugin.getServer().getOnlinePlayers());
                break;
            case 0:
                players.add(catcher);
                break;
            default:
                Location loc = catcher.getLocation();

                for (Player player : catcher.getWorld().getPlayers()) {
                    if (player.getLocation().distance(loc) <= announceValue) {
                        players.add(player);
                    }
                }
        }

        if (plugin.getConfig().getBoolean("messages.only-announce-fishing-rod")) {
            players.removeIf(player -> player.getInventory().getItemInMainHand().getType() != Material.FISHING_ROD);
        }

        return players;
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

    private void launchFirework(Location loc) {
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.AQUA)
                .withFade(Color.BLUE)
                .withTrail()
                .withFlicker()
                .build();
        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
    }
}
