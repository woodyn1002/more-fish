package me.elsiff.morefish.commands;

import me.elsiff.morefish.managers.ContestManager;
import me.elsiff.morefish.MoreFish;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralCommands implements CommandExecutor, TabCompleter {
    private final MoreFish plugin;
    private final ContestManager contest;

    public GeneralCommands(MoreFish plugin) {
        this.plugin = plugin;
        this.contest = plugin.getContestManager();
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<String>();

        if (args.length < 2) {
            if (sender.hasPermission("morefish.admin")) {
                list.add("help");
                list.add("start");
                list.add("stop");
                list.add("clear");
                list.add("rewards");
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
            String prefix = "§b[MoreFish]§r ";
            sender.sendMessage(prefix + "§3> ===== §b§lMoreFish §bv" + plugin.getDescription().getVersion() + "§3 ===== <");
            sender.sendMessage(prefix + "/" + label + " help");
            sender.sendMessage(prefix + "/" + label + " start [runningTime]");
            sender.sendMessage(prefix + "/" + label + " stop");
            sender.sendMessage(prefix + "/" + label + " rewards");
            sender.sendMessage(prefix + "/" + label + " clear");
            sender.sendMessage(prefix + "/" + label + " reload");
            sender.sendMessage(prefix + "/" + label + " top");

            return true;
        } else if (args[0].equalsIgnoreCase("start")) {

            if (!sender.hasPermission("morefish.admin")) {
                sender.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            if (contest.hasStarted()) {
                sender.sendMessage(plugin.getLocale().getString("already-ongoing"));
                return true;
            }

            boolean hasTimer = false;
            long sec = 0;

            if (args.length > 1) {
                try {
                    sec = Long.parseLong(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(String.format(plugin.getLocale().getString("not-number"), args[1]));
                    return true;
                }

                if (sec <= 0) {
                    sender.sendMessage(plugin.getLocale().getString("not-positive"));
                    return true;
                }

                hasTimer = true;
                contest.startWithTimer(sec);
            } else {
                contest.start();
            }


            String msg = plugin.getLocale().getString("contest-start");
            boolean broadcast = plugin.getConfig().getBoolean("messages.broadcast-start");

            if (broadcast) {
                plugin.getServer().broadcastMessage(msg);
            } else {
                sender.sendMessage(msg);
            }

            if (hasTimer) {
                String msgTimer = plugin.getLocale().getString("contest-start-timer")
                        .replaceAll("%sec%", sec + "")
                        .replaceAll("%time%", plugin.getTimeString(sec));

                if (broadcast) {
                    plugin.getServer().broadcastMessage(msgTimer);
                } else {
                    sender.sendMessage(msgTimer);
                }
            }

            return true;
        } else if (args[0].equalsIgnoreCase("stop")) {

            if (!sender.hasPermission("morefish.admin")) {
                sender.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            if (!contest.hasStarted()) {
                sender.sendMessage(plugin.getLocale().getString("already-stopped"));
                return true;
            }


            String msg = plugin.getLocale().getString("contest-stop");
            boolean showRanking = plugin.getConfig().getBoolean("messages.show-top-on-ending");
            boolean broadcast = plugin.getConfig().getBoolean("messages.broadcast-stop");

            if (broadcast) {
                plugin.getServer().broadcastMessage(msg);
            } else {
                sender.sendMessage(msg);
            }

            if (showRanking) {
                sendRankingMessage(sender, broadcast);
            }


            contest.stop();

            return true;
        } else if (args[0].equalsIgnoreCase("clear")) {

            if (!sender.hasPermission("morefish.admin")) {
                sender.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            if (!contest.hasStarted()) {
                sender.sendMessage(plugin.getLocale().getString("not-ongoing"));
                return true;
            }

            contest.clearRecords();

            sender.sendMessage(plugin.getLocale().getString("clear-records"));

            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("morefish.admin")) {
                sender.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            plugin.reloadConfig();
            plugin.getFishManager().loadFishList();

            sender.sendMessage(plugin.getLocale().getString("reload-config"));

            return true;
        } else if (args[0].equalsIgnoreCase("top")) {

            if (!sender.hasPermission("morefish.top")) {
                sender.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            if (!contest.hasStarted()) {
                sender.sendMessage(plugin.getLocale().getString("not-ongoing"));
                return true;
            }

            if (contest.getRecordAmount() < 1) {
                String msg = plugin.getLocale().getString("top-no-record");
                sender.sendMessage(msg);
            } else {
                sendRankingMessage(sender, false);
            }

            return true;
        } else if (args[0].equalsIgnoreCase("rewards")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getLocale().getString("in-game-command"));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("morefish.admin")) {
                player.sendMessage(plugin.getLocale().getString("no-permission"));
                return true;
            }

            plugin.getRewardsGUI().openGUI(player);

            return true;
        } else {
            sender.sendMessage(plugin.getLocale().getString("invalid-command"));

            return true;
        }
    }

    private void sendRankingMessage(CommandSender sender, boolean broadcast) {
        String format = plugin.getLocale().getString("top-list");
        int limit = plugin.getConfig().getInt("messages.top-number");

        for (int i = 1; i < limit + 1; i ++) {
            ContestManager.Record record = contest.getRecord(i);

            if (record == null)
                break;

            String msg = format.replaceAll("%ordinal%", plugin.getOrdinal(i))
                    .replaceAll("%number%", i + "")
                    .replaceAll("%player%", record.getPlayer().getName())
                    .replaceAll("%length%", record.getLength() + "")
                    .replaceAll("%fish%", record.getFish().getName());

            if (broadcast) {
                plugin.getServer().broadcastMessage(msg);
            } else {
                sender.sendMessage(msg);
            }
        }

        if (broadcast) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                sendPrivateRankingMessage(player);
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                sendPrivateRankingMessage(player);
            }
        }
    }

    private void sendPrivateRankingMessage(Player player) {
        String msg;

        if (plugin.getContestManager().hasRecord(player)) {
            int number = contest.getNumber(player);
            ContestManager.Record record = contest.getRecord(number);

            msg = plugin.getLocale().getString("top-mine")
                    .replaceAll("%ordinal%", plugin.getOrdinal(number))
                    .replaceAll("%number%", number + "")
                    .replaceAll("%player%", record.getPlayer().getName())
                    .replaceAll("%length%", record.getLength() + "")
                    .replaceAll("%fish%", record.getFish().getName());
        } else {
            msg = plugin.getLocale().getString("top-mine-no-record");
        }

        player.sendMessage(msg);
    }
}
