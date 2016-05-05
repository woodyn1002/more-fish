package me.elsiff.morefish;

import org.bukkit.OfflinePlayer;

import java.util.*;

public class ContestManager {
	private final MoreFish plugin;
	private final RecordComparator comparator = new RecordComparator();
	private final List<Record> recordList = new ArrayList<Record>();
	private boolean hasStarted = false;

	public ContestManager(MoreFish plugin) {
		this.plugin = plugin;

		if (plugin.getConfig().getBoolean("contest.auto-start")) {
			hasStarted = true;
		}
	}

	public boolean hasStarted() {
		return hasStarted;
	}

	public void start() {
		hasStarted = true;
	}

	public void stop() {
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

	public Record getRecord(int ranking) {
		return ((recordList.size() >= ranking) ? recordList.get(ranking - 1) : null);
	}

	public int getRecordAmount() {
		return recordList.size();
	}

	public double getRecordLength(OfflinePlayer player) {
		for (Record record : recordList) {
			if (record.getPlayer().equals(player)) {
				return record.getLength();
			}
		}

		return 0.0D;
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
}
