package me.elsiff.morefish;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ContestManager {
    private final MoreFish plugin;
    private final RecordComparator comparator = new RecordComparator();
    private final List<Record> recordList = new ArrayList<Record>();
    private boolean hasStarted = false;
    private BukkitTask task = null;

    public ContestManager(MoreFish plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("general.auto-start")) {
            hasStarted = true;
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

        recordList.clear();
        hasStarted = false;
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
