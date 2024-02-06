package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Created by elsiff on 2019-01-19.
 */
class FishingCompetitionTimerBarHandler(
    private val plugin: Plugin
) {
    private var timerBarKey = NamespacedKey(plugin, "fishing-competition-timer-bar")
    private var timerBar: BossBar? = null
    private var barUpdatingTask: BukkitTask? = null
    private var barDisplayer: TimerBarDisplayer? = null

    val hasTimerEnabled: Boolean
        get() = timerBar != null

    fun enableTimer(duration: Long) {
        val barColor = BarColor.valueOf(Config.standard.string("messages.contest-bar-color").toUpperCase())
        timerBar = plugin.server
            .createBossBar(timerBarKey, "", barColor, BarStyle.SEGMENTED_10)
            .apply {
                for (player in plugin.server.onlinePlayers)
                    addPlayer(player)
            }

        barUpdatingTask = TimerBarUpdater(duration).runTaskTimer(plugin, 0, 20L)
        barDisplayer = TimerBarDisplayer().also {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    fun disableTimer() {
        barUpdatingTask!!.cancel()
        barUpdatingTask = null

        timerBar!!.apply {
            setTitle(timerBarTitle(0))
            progress = 0.0
            removeAll()
        }

        barDisplayer?.let { HandlerList.unregisterAll(it) }
        barDisplayer = null

        plugin.server.removeBossBar(timerBarKey)
        timerBar = null
    }

    private fun timerBarTitle(remainingSeconds: Long): String =
        Lang.format("timer-boss-bar")
            .replace("%time%" to Lang.time(remainingSeconds))
            .output()

    private inner class TimerBarUpdater(
        private val duration: Long
    ) : BukkitRunnable() {
        private var remainingSeconds: Long = duration

        override fun run() {
            remainingSeconds--
            timerBar!!.run {
                setTitle(timerBarTitle(remainingSeconds))
                progress = remainingSeconds.toDouble() / duration
            }
        }
    }

    private inner class TimerBarDisplayer : Listener {
        @EventHandler
        fun onPlayerJoin(event: PlayerJoinEvent) =
            timerBar!!.addPlayer(event.player)

        @EventHandler
        fun onPlayerQuit(event: PlayerQuitEvent) =
            timerBar!!.removePlayer(event.player)
    }
}
