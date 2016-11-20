package me.elsiff.morefish.managers;

import me.elsiff.morefish.MoreFish;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {
    private final MoreFish plugin;
    private BossBar timerBar;

    public BossBarManager(MoreFish plugin) {
        this.plugin = plugin;
    }

    public void createTimerBar(long sec) {
        String title = plugin.getLocale().getString("timer-boss-bar")
                .replaceAll("%time%", plugin.getTimeString(sec));
        BarColor color = BarColor.valueOf(plugin.getConfig().getString("messages.contest-bar-color").toUpperCase());

        timerBar = plugin.getServer().createBossBar(title, color, BarStyle.SEGMENTED_10);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            timerBar.addPlayer(player);
        }
    }

    public void removeTimerBar() {
        timerBar.removeAll();
        timerBar = null;
    }

    public void addPlayer(Player player) {
        timerBar.addPlayer(player);
    }

    public void updateTimerBar(long passed, long timer) {
        long left = timer - passed;
        String title = plugin.getLocale().getString("timer-boss-bar")
                .replaceAll("%time%", plugin.getTimeString(left));

        timerBar.setTitle(title);
        timerBar.setProgress((double) left / timer);
    }
}
