package me.elsiff.morefish.commands;

import me.elsiff.morefish.ContestManager;
import me.elsiff.morefish.MoreFish;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralCommands implements CommandExecutor, TabCompleter {
	private final MoreFish plugin;
	private final ContestManager contest;
	private final String prefix = "§b[MoreFish]§r ";

	public GeneralCommands(MoreFish plugin) {
		this.plugin = plugin;
		this.contest = plugin.getContestManager();
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();

		if (args.length < 2 ) {
			if (sender.hasPermission("morefish.admin")) {
				list.add("help");
				list.add("start");
				list.add("stop");
				list.add("clear");
			}

			if (sender.hasPermission("morefish.top")) {
				list.add("top");
			}
		}

		String finalArg = args[args.length - 1];
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			if (!it.next().startsWith(finalArg)) {
				it.remove();
			}
		}

		return list;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(prefix + "§3> ===== §b§lMoreFish §bv" + plugin.getDescription().getVersion() + "§3 ===== <");
			sender.sendMessage(prefix + "/" + label + " help");
			sender.sendMessage(prefix + "/" + label + " start (running time (sec))");
			sender.sendMessage(prefix + "/" + label + " stop");
			sender.sendMessage(prefix + "/" + label + " clear");
			sender.sendMessage(prefix + "/" + label + " reload");
			sender.sendMessage(prefix + "/" + label + " top");

			return true;
		} else if (args[0].equalsIgnoreCase("start")) {

			if (!sender.hasPermission("morefish.admin")) {
				sender.sendMessage(prefix + "You don't have the permission.");
				return true;
			}

			if (contest.hasStarted()) {
				sender.sendMessage(prefix + "The contest is already ongoing.");
				return true;
			}

			contest.start();

			plugin.getServer().broadcastMessage(prefix + "The fishing contest has started!");

			return true;
		} else if (args[0].equalsIgnoreCase("stop")) {

			if (!sender.hasPermission("morefish.admin")) {
				sender.sendMessage(prefix + "You don't have the permission.");
				return true;
			}

			if (!contest.hasStarted()) {
				sender.sendMessage(prefix + "The contest is already stopped.");
				return true;
			}

			contest.start();

			plugin.getServer().broadcastMessage(prefix + "The fishing contest has stopped!");

			int limit = plugin.getConfig().getInt("contest.ranking-limit");
			for (int i = 1; i < limit + 1; i ++) {
				ContestManager.Record record = contest.getRecord(i);

				if (record == null)
					break;

				plugin.getServer().broadcastMessage(prefix +
						"§9§l" + getOrdinal(i) + "§9:" + record.getPlayer().getName() + ", " + record.getLength() + "cm " + record.getFish().getName());
			}

			return true;
		} else if (args[0].equalsIgnoreCase("clear")) {

			if (!sender.hasPermission("morefish.admin")) {
				sender.sendMessage(prefix + "You don't have the permission.");
				return true;
			}

			if (!contest.hasStarted()) {
				sender.sendMessage(prefix + "The contest isn't ongoing now.");
				return true;
			}

			contest.clearRecords();

			sender.sendMessage(prefix + "The records has been cleared successfully.");

			return true;
		} else if (args[0].equalsIgnoreCase("reload")) {

			if (!sender.hasPermission("morefish.admin")) {
				sender.sendMessage(prefix + "You don't have the permission.");
				return true;
			}

			plugin.reloadConfig();
			plugin.getFishManager().loadFishList();

			sender.sendMessage(prefix + "Reloaded the config successfully.");

			return true;
		} else if (args[0].equalsIgnoreCase("top")) {

			if (!sender.hasPermission("morefish.top")) {
				sender.sendMessage(prefix + "You don't have the permission.");
				return true;
			}

			if (!contest.hasStarted()) {
				sender.sendMessage(prefix + "The contest isn't ongoing now.");
				return true;
			}

			if (contest.getRecordAmount() < 1) {
				sender.sendMessage(prefix + "Nobody made any record yet.");
			} else {
				int limit = plugin.getConfig().getInt("contest.ranking-limit");
				for (int i = 1; i < limit + 1; i ++) {
					ContestManager.Record record = contest.getRecord(i);

					if (record == null)
						break;

					sender.sendMessage(prefix +
							"§e" + getOrdinal(i) + "§7: " + record.getPlayer().getName() + ", " + record.getLength() + "cm " + record.getFish().getName());
				}
			}

			return true;
		}

		return false;
	}

	private String getOrdinal(int number) {
		switch (number) {
			case 1:
				return "1st";
			case 2:
				return "2nd";
			case 3:
				return "3rd";
			default:
				return number + "th";
		}
	}
}
