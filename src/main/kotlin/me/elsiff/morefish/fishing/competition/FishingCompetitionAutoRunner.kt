package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.configuration.Config
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.time.LocalTime

/**
 * Created by elsiff on 2019-01-18.
 */
class FishingCompetitionAutoRunner(
    private val plugin: Plugin,
    private val competitionHost: FishingCompetitionHost
) {
    private lateinit var scheduledTimes: Collection<LocalTime>
    private var timeCheckingTask: BukkitTask? = null

    val isEnabled: Boolean
        get() = timeCheckingTask != null

    fun setScheduledTimes(scheduledTimes: Collection<LocalTime>) {
        this.scheduledTimes = scheduledTimes
    }

    fun enable() {
        check(timeCheckingTask == null) { "Auto runner must not be already enabled" }

        timeCheckingTask = TimeChecker(this::tryOpenCompetition)
            .runTaskTimer(plugin, 0, HALF_MINUTE)
    }

    fun disable() {
        check(timeCheckingTask != null) { "Auto runner must not be disabled" }

        if (timeCheckingTask != null) {
            timeCheckingTask!!.cancel()
            timeCheckingTask = null
        }
    }

    private fun tryOpenCompetition() {
        val requiredPlayers = Config.standard.int("auto-running.required-players")
        if (!competitionHost.competition.isEnabled() && plugin.server.onlinePlayers.size >= requiredPlayers) {
            val duration = Config.standard.long("auto-running.timer") * 20
            competitionHost.openCompetitionFor(duration)
        }
    }

    private inner class TimeChecker(
        private val work: () -> Unit
    ) : BukkitRunnable() {
        override fun run() {
            val currentTime = LocalTime.now().withSecond(0).withNano(0)
            for (scheduledTime in scheduledTimes) {
                if (scheduledTime == currentTime) {
                    work()
                }
            }
        }
    }

    companion object {
        const val HALF_MINUTE: Long = 30 * 20
    }
}