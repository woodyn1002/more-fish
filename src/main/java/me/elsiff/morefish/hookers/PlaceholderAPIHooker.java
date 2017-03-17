package me.elsiff.morefish.hookers;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.elsiff.morefish.MoreFish;
import me.elsiff.morefish.managers.ContestManager;
import org.bukkit.entity.Player;

public class PlaceholderAPIHooker extends EZPlaceholderHook {
    private final MoreFish plugin;
    private final ContestManager contest;

    public PlaceholderAPIHooker(MoreFish plugin) {
        super(plugin, "morefish");
        this.plugin = plugin;
        this.contest = plugin.getContestManager();
        this.hook();
    }

    @Override
    public String onPlaceholderRequest(Player player, String str) {
        if (str.startsWith("top_player_")) {
            if (!contest.hasStarted()) {
                return "";
            }
            int number = Integer.parseInt(str.substring(11));

            if (number > contest.getRecordAmount()) {
                return "";
            }
            ContestManager.Record record = contest.getRecord(number);

            return record.getPlayer().getName();
        } else if (str.startsWith("top_fish_")) {
            if (!contest.hasStarted()) {
                return "";
            }
            int number = Integer.parseInt(str.substring(9));

            if (number > contest.getRecordAmount()) {
                return "";
            }
            ContestManager.Record record = contest.getRecord(number);

            return record.getFishName();
        } else if (str.startsWith("top_length_")) {
            if (!contest.hasStarted()) {
                return "";
            }
            int number = Integer.parseInt(str.substring(11));

            if (number > contest.getRecordAmount()) {
                return "";
            }
            ContestManager.Record record = contest.getRecord(number);

            return record.getLength() + "";
        } else if (str.startsWith("rank") && player != null) {
            if (!contest.hasStarted()) {
                return "";
            }

            return (contest.hasRecord(player) ? contest.getNumber(player) : 0) + "";
        }

        return null;
    }
}