package me.elsiff.morefish;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ContestManager {
    private final MoreFish plugin;
    private final RecordComparator comparator = new RecordComparator();
    private final List<Record> recordList = new ArrayList<Record>();
    private final File fileRewards;
    private final FileConfiguration configRewards;
    private boolean hasStarted = false;
    private BukkitTask task = null;

    public ContestManager(MoreFish plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("general.auto-start")) {
            hasStarted = true;
        }

        fileRewards = new File(plugin.getDataFolder(), "rewards.yml");

        if (!fileRewards.exists()) {
            try {
                boolean created = fileRewards.createNewFile();

                if (!created) {
                    plugin.getLogger().warning("Failed to create rewards.yml!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configRewards = YamlConfiguration.loadConfiguration(fileRewards);
    }

    private void saveRewards() {
        try {
            configRewards.save(fileRewards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void start() {
        hasStarted = true;
    }

    public void startWithTimer(long sec) {
        task = new TimerTask().runTaskLater(plugin, 20 * sec);

        start();
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        giveRewards();

        recordList.clear();
        hasStarted = false;
    }

    private void giveRewards() {
        ItemStack[] rewards = getRewards();

        for (int i = 0; i < rewards.length && i < recordList.size(); i ++) {
            ItemStack stack = rewards[i];
            int number = i + 1;
            OfflinePlayer oPlayer = getRecord(number).getPlayer();

            if (!oPlayer.isOnline()) {
                plugin.getLogger().info("The " + oPlayer.getName() + "'s reward of fishing contest has not sent as the player is offline now.");
                continue;
            }

            Player player = oPlayer.getPlayer();

            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(stack);
            } else {
                player.getWorld().dropItem(player.getLocation(), stack);
            }

            String msg = plugin.getConfig().getString("messages.contest-reward.text");
            msg = ChatColor.translateAlternateColorCodes('&', msg);

            msg = msg.replaceAll("%player%", player.getName())
                    .replaceAll("%item%", getItemName(stack))
                    .replaceAll("%ordinal%", plugin.getOrdinal(number))
                    .replaceAll("%number%", number + "");

            player.sendMessage(msg);
        }
    }

    private String getItemName(ItemStack item) {
        return ((item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
                item.getItemMeta().getDisplayName() : item.getType().name().toLowerCase().replaceAll("_", " "));
    }

    public ItemStack[] getRewards() {
        ItemStack[] rewards = new ItemStack[8];

        for (String path : configRewards.getKeys(false)) {
            int i = Integer.parseInt(path.substring(7));
            ItemStack item = configRewards.getItemStack("reward_" + i);

            rewards[i] = item;
        }

        return rewards;
    }

    public void setRewards(ItemStack[] rewards) {
        for (int i = 0; i < rewards.length; i ++) {
            configRewards.set("reward_" + i, rewards[i]);
        }

        saveRewards();
    }

    public boolean isNew1st(CaughtFish fish) {
        Record record = getRecord(1);

        return (record == null || record.getLength() < fish.getLength());
    }

    public void addRecord(OfflinePlayer player, CaughtFish fish) {
        ListIterator<Record> it = recordList.listIterator();
        while (it.hasNext()) {
            Record record = it.next();

            if (record.getPlayer().equals(player)) {
                if (record.getLength() < fish.getLength()) {
                    it.remove();
                    break;
                } else {
                    return;
                }
            }
        }

        recordList.add(new Record(player, fish));

        Collections.sort(recordList, comparator);
    }

    public Record getRecord(int number) {
        return ((recordList.size() >= number) ? recordList.get(number - 1) : null);
    }

    public int getRecordAmount() {
        return recordList.size();
    }

    public boolean hasRecord(OfflinePlayer player) {
        for (Record record : recordList) {
            if (record.getPlayer().equals(player)) {
                return true;
            }
        }

        return false;
    }

    public double getRecordLength(OfflinePlayer player) {
        for (Record record : recordList) {
            if (record.getPlayer().equals(player)) {
                return record.getLength();
            }
        }

        return 0.0D;
    }

    public int getNumber(OfflinePlayer player) {
        for (int i = 0; i < recordList.size(); i ++) {
            if (recordList.get(i).getPlayer().equals(player)) {
                return (i + 1);
            }
        }

        return 0;
    }

    public void clearRecords() {
        recordList.clear();
    }

    private class RecordComparator implements Comparator<Record> {

        public int compare(Record arg0, Record arg1) {
            return ((arg0.getLength() < arg1.getLength()) ? 1 : ((arg0.getLength() > arg1.getLength()) ? -1 : 0));
        }
    }

    public class Record {
        private final OfflinePlayer player;
        private final CaughtFish fish;

        public Record(OfflinePlayer player, CaughtFish fish) {
            this.player = player;
            this.fish = fish;
        }

        public OfflinePlayer getPlayer() {
            return player;
        }

        public CaughtFish getFish() {
            return fish;
        }

        public double getLength() {
            return fish.getLength();
        }
    }

    private class TimerTask extends BukkitRunnable {

        public void run() {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "morefish stop");
        }
    }
}
