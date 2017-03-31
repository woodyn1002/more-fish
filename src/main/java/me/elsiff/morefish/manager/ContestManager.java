package me.elsiff.morefish.manager;

import me.elsiff.morefish.CaughtFish;
import me.elsiff.morefish.MoreFish;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ContestManager {
    private final MoreFish plugin;
    private final RecordComparator comparator = new RecordComparator();
    private final List<Record> recordList = new ArrayList<>();
    private final File fileRewards;
    private final FileConfiguration configRewards;
    private File fileRecords;
    private FileConfiguration configRecords;
    private boolean hasStarted = false;
    private TimerTask task = null;

    public ContestManager(MoreFish plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("general.auto-start")) {
            hasStarted = true;
        }

        fileRewards = new File(plugin.getDataFolder(), "rewards.yml");

        createFile(fileRewards);
        configRewards = YamlConfiguration.loadConfiguration(fileRewards);

        if (plugin.getConfig().getBoolean("general.save-records")) {
            fileRecords = new File(plugin.getDataFolder(), "records.yml");
            createFile(fileRecords);
            configRecords = YamlConfiguration.loadConfiguration(fileRecords);

            loadRecords();
        }
    }

    private void createFile(File file) {
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();

                if (!created) {
                    plugin.getLogger().warning("Failed to create " + file.getName() + "!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveRewards() {
        try {
            configRewards.save(fileRewards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecords() {
        recordList.clear();

        for (String path : configRecords.getKeys(false)) {
            UUID id = UUID.fromString(configRecords.getString(path + ".player"));
            String fishName = configRecords.getString(path + ".fish-name");
            double length = configRecords.getDouble(path + ".length");

            recordList.add(new Record(id, fishName, length));
        }

        Collections.sort(recordList, comparator);
    }

    public void saveRecords() {
        for (String path : configRecords.getKeys(false)) {
            configRecords.set(path, null);
        }

        for (int i = 0; i < recordList.size(); i ++) {
            Record record = recordList.get(i);
            configRecords.set(i + ".player", record.getPlayer().getUniqueId().toString());
            configRecords.set(i + ".fish-name", record.getFishName());
            configRecords.set(i + ".length", record.getLength());
        }

        try {
            configRecords.save(fileRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public boolean hasTimer() {
        return (task != null);
    }

    public void start() {
        hasStarted = true;
    }

    public void startWithTimer(long sec) {
        task = new TimerTask(sec);
        task.runTaskTimer(plugin, 20, 20);

        if (plugin.hasBossBar()) {
            plugin.getBossBarManager().createTimerBar(sec);
        }

        start();
    }

    public void stop() {
        if (task != null) {
            if (plugin.hasBossBar()) {
                plugin.getBossBarManager().removeTimerBar();
            }

            task.cancel();
            task = null;
        }

        giveRewards();

        if (!plugin.getConfig().getBoolean("general.save-records")) {
            recordList.clear();
        }

        hasStarted = false;
    }

    private void giveRewards() {
        Set<Integer> receivers = new HashSet<>();

        ItemStack[] rewards = getRewards();
        for (int i = 0; i < rewards.length - 1 && i < recordList.size(); i ++) {
            ItemStack stack = rewards[i];

            if (stack == null || stack.getType() == Material.AIR)
                continue;

            OfflinePlayer player = getRecord(i + 1).getPlayer();
            sendReward(player, stack);

            receivers.add(i);
        }

        if (plugin.hasEconomy()) {
            double[] cashPrizes = getCashPrizes();
            for (int i = 0; i < cashPrizes.length - 1 && i < recordList.size(); i ++) {
                double amount = cashPrizes[i];

                if (amount == 0.0D)
                    continue;

                OfflinePlayer player = getRecord(i + 1).getPlayer();
                sendCashPrize(player, amount);

                receivers.add(i);
            }

            if (cashPrizes[7] != 0.0D) {
                for (int i = 0; i < getRecordAmount(); i ++) {
                    if (receivers.contains(i))
                        continue;

                    Record record = getRecord(i + 1);

                    sendCashPrize(record.getPlayer(), cashPrizes[7]);
                }
            }
        }

        if (rewards[7] != null) {
            for (int i = 0; i < getRecordAmount(); i ++) {
                if (receivers.contains(i))
                    continue;

                Record record = getRecord(i + 1);
                sendReward(record.getPlayer(), rewards[7]);
            }
        }
    }

    private void sendReward(OfflinePlayer oPlayer, ItemStack stack) {
        if (!oPlayer.isOnline()) {
            plugin.getLogger().info(oPlayer.getName() + "'s reward of fishing contest has not been sent as the player is offline now.");
            return;
        }

        Player player = oPlayer.getPlayer();

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(stack);
        } else {
            player.getWorld().dropItem(player.getLocation(), stack);
        }

        int number = getNumber(player);
        String msg = plugin.getLocale().getString("reward");

        msg = msg.replaceAll("%player%", player.getName())
                .replaceAll("%item%", getItemName(stack))
                .replaceAll("%ordinal%", plugin.getOrdinal(number))
                .replaceAll("%number%", number + "");

        player.sendMessage(msg);
    }
    
    private void sendCashPrize(OfflinePlayer player, double amount) {
        if (!plugin.getVaultHooker().getEconomy().hasAccount(player)) {
            plugin.getLogger().info(player.getName() + "'s reward of fishing contest has not been sent as having no economy account.");
            return;
        } else {
            plugin.getVaultHooker().getEconomy().depositPlayer(player, amount);
        }

        if (player.isOnline()) {
            int number = getNumber(player);
            String msg = plugin.getLocale().getString("reward-cash-prize");

            msg = msg.replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", amount + "")
                    .replaceAll("%ordinal%", plugin.getOrdinal(number))
                    .replaceAll("%number%", number + "");

            player.getPlayer().sendMessage(msg);
        }
    }

    private String getItemName(ItemStack item) {
        return ((item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
                item.getItemMeta().getDisplayName() : item.getType().name().toLowerCase().replaceAll("_", " "));
    }

    public ItemStack[] getRewards() {
        ItemStack[] rewards = new ItemStack[8];

        for (String path : configRewards.getKeys(false)) {
            if (!path.startsWith("reward_"))
                continue;

            int i = Integer.parseInt(path.substring(7));
            ItemStack item = configRewards.getItemStack("reward_" + i);

            rewards[i] = item;
        }

        return rewards;
    }

    public double[] getCashPrizes() {
        double[] arr = new double[8];

        for (String path : configRewards.getKeys(false)) {
            if (!path.startsWith("cash-prize_"))
                continue;

            int i = Integer.parseInt(path.substring(11));
            double amount = configRewards.getDouble("cash-prize_" + i);

            arr[i] = amount;
        }

        return arr;
    }

    public void setRewards(ItemStack[] rewards) {
        for (int i = 0; i < rewards.length; i ++) {
            configRewards.set("reward_" + i, rewards[i]);
        }

        saveRewards();
    }

    public void setCashPrizes(double[] arr) {
        for (int i = 0; i < arr.length; i ++) {
            configRewards.set("cash-prize_" + i, arr[i]);
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

        recordList.add(new Record(player.getUniqueId(), fish));

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
            if (recordList.get(i).getPlayer().getUniqueId().equals(player.getUniqueId())) {
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
        private final UUID id;
        private final String fishName;
        private final double length;

        public Record(UUID id, CaughtFish fish) {
            this(id, fish.getName(), fish.getLength());
        }

        public Record(UUID id, String fishName, double length) {
            this.id = id;
            this.fishName = fishName;
            this.length = length;
        }

        public OfflinePlayer getPlayer() {
            return plugin.getServer().getOfflinePlayer(id);
        }

        public String getFishName() {
            return fishName;
        }

        public double getLength() {
            return length;
        }
    }

    private class TimerTask extends BukkitRunnable {
        private long passed = 0;
        private final long timer;

        public TimerTask(long sec) {
            this.timer = sec;
        }

        public void run() {
            passed ++;

            if (plugin.hasBossBar()) {
                plugin.getBossBarManager().updateTimerBar(passed, timer);
            }

            if (passed >= timer) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "morefish stop");
                this.cancel();
            }
        }
    }
}
