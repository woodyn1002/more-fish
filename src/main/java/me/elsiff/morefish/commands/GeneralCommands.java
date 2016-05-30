package me.elsiff.morefish.commands;

import me.elsiff.morefish.ContestManager;
import me.elsiff.morefish.MoreFish;
import org.bukkit.ChatColor;
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
    private final String prefix;

    public GeneralCommands(MoreFish plugin) {
        this.plugin = plugin;
        this.contest = plugin.getContestManager();
        this.prefix = plugin.prefix;
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
            sender.sendMessage(prefix + "/" + label + " start (runningTime)");
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

            boolean hasTimer = false;
            long sec = 0;

            if (args.length > 1) {
                try {
                    sec = Long.parseLong(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(prefix + "'" + args[1] + "' is invalid number.");
                    return true;
                }

                if (sec <= 0) {
                    sender.sendMessage(prefix + "Only positive number is available.");
                    return true;
                }

                hasTimer = true;
                contest.startWithTimer(sec);
            } else {
                contest.start();
            }


            String msg = plugin.getConfig().getString("messages.contest-start.text");
            msg = ChatColor.translateAlternateColorCodes('&', msg);

            boolean broadcast = plugin.getConfig().getBoolean("messages.contest-start.broadcast");

            if (broadcast) {
                plugin.getServer().broadcastMessage(msg);
            } else {
                sender.sendMessage(msg);
            }

            if (hasTimer) {
                String msgTimer = plugin.getConfig().getString("messages.contest-start.text-timer");
                msgTimer = ChatColor.translateAlternateColorCodes('&', msgTimer);

                msgTimer = msgTimer.replaceAll("%sec%", sec + "")
                        .replaceAll("%time%", getTimeString(sec));

                if (broadcast) {
                    plugin.getServer().broadcastMessage(msgTimer);
                } else {
                    sender.sendMessage(msgTimer);
                }
            }

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


            String msg = plugin.getConfig().getString("messages.contest-stop.text");
            msg = ChatColor.translateAlternateColorCodes('&', msg);

            boolean showRanking = plugin.getConfig().getBoolean("messages.contest-stop.show-ranking");
            boolean broadcast = plugin.getConfig().getBoolean("messages.contest-stop.broadcast");

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
                String msg = plugin.getConfig().getString("messages.contest-top.no-record");
                sender.sendMessage(msg);
            } else {
                sendRankingMessage(sender, false);
            }

            return true;
        }

        return false;
    }

    private String getTimeString(long sec) {
        String str = "";

        int minutes = (int) (sec / 60);
        int second = (int) (sec - minutes * 60);

        if (minutes > 0) {
            str += minutes + "m ";
        }

        str += second + "s";

        return str;
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

    private void sendRankingMessage(CommandSender sender, boolean broadcast) {
        String format = plugin.getConfig().getString("messages.contest-top.text");
        format = ChatColor.translateAlternateColorCodes('&', format);

        int limit = plugin.getConfig().getInt("messages.contest-top.ranking-limit");

        for (int i = 1; i < limit + 1; i ++) {
            ContestManager.Record record = contest.getRecord(i);

            if (record == null)
                break;

            String msg = format.replaceAll("%ordinal%", getOrdinal(i))
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

            msg = plugin.getConfig().getString("messages.contest-top.my-record.text")
                    .replaceAll("%ordinal%", getOrdinal(number))
                    .replaceAll("%number%", number + "")
                    .replaceAll("%player%", record.getPlayer().getName())
                    .replaceAll("%length%", record.getLength() + "")
                    .replaceAll("%fish%", record.getFish().getName());
        } else {
            msg = plugin.getConfig().getString("messages.contest-top.my-record.no-record");
        }

        msg = ChatColor.translateAlternateColorCodes('&', msg);
        player.sendMessage(msg);
    }
}
