package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.ConfigurationSectionAccessor
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.util.NumberUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import kotlin.math.min

/**
 * Created by elsiff on 2019-01-18.
 */
class FishingCompetitionHost(
    private val plugin: Plugin,
    val competition: FishingCompetition
) {
    private val timerBarHandler: FishingCompetitionTimerBarHandler = FishingCompetitionTimerBarHandler(plugin)
    private var timerTask: BukkitTask? = null

    private val msgConfig: ConfigurationSectionAccessor
        get() = Config.standard["messages"]

    val prizes: Map<IntRange, Prize>
        get() = Config.prizeMapLoader.loadFrom(Config.standard, "contest-prizes")

    fun openCompetition() {
        competition.enable()

        if (msgConfig.boolean("broadcast-start")) {
            plugin.server.broadcastMessage(Lang.text("contest-start"))
        }
    }

    fun openCompetitionFor(tick: Long) {
        val duration = tick / 20
        competition.enable()
        val closingWork = { closeCompetition() }
        timerTask = plugin.server.scheduler.runTaskLater(plugin, closingWork, tick)

        if (Config.standard.boolean("general.use-boss-bar")) {
            timerBarHandler.enableTimer(duration)
        }

        if (msgConfig.boolean("broadcast-start")) {
            plugin.server.broadcastMessage(Lang.text("contest-start"))

            val msg = Lang.format("contest-start-timer")
                .replace("%time%" to Lang.time(duration))
                .output()
            plugin.server.broadcastMessage(msg)
        }
    }

    fun closeCompetition(suspend: Boolean = false) {
        competition.disable()
        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null

            if (timerBarHandler.hasTimerEnabled) {
                timerBarHandler.disableTimer()
            }
        }

        val broadcast = msgConfig.boolean("broadcast-stop")
        if (broadcast) {
            plugin.server.broadcastMessage(Lang.text("contest-stop"))
        }
        if (!suspend) {
            if (prizes.isNotEmpty()) {
                val ranking = competition.ranking
                for ((range, prize) in prizes) {
                    val rangeInIndex = IntRange(
                        start = range.start - 1,
                        endInclusive = min(range.endInclusive - 1, ranking.lastIndex)
                    )
                    for (record in ranking.slice(rangeInIndex)) {
                        val rankNumber = competition.rankNumberOf(record)
                        prize.giveTo(record.fisher, rankNumber, plugin)
                    }
                }
            }

            if (broadcast && msgConfig.boolean("show-top-on-ending")) {
                for (player in plugin.server.onlinePlayers) {
                    informAboutRanking(player)
                }
            }
        }
        if (!Config.standard.boolean("general.save-records")) {
            competition.clearRecords()
        }
    }

    fun informAboutRanking(receiver: CommandSender) {
        if (competition.ranking.isEmpty()) {
            receiver.sendMessage(Lang.text("top-no-record"))
        } else {
            val topSize = msgConfig.int("top-number")
            competition.top(topSize).forEachIndexed { index, record ->
                val number = index + 1
                val msg = Lang.format("top-list")
                    .replace(topReplacementOf(number, record)).output()
                receiver.sendMessage(msg)
            }

            if (receiver is Player) {
                if (!competition.containsContestant(receiver)) {
                    receiver.sendMessage(Lang.text("top-mine-no-record"))
                } else {
                    competition.rankedRecordOf(receiver).let {
                        val msg = Lang.format("top-mine")
                            .replace(topReplacementOf(it.first, it.second)).output(receiver)
                        receiver.sendMessage(msg)
                    }
                }
            }
        }
    }

    private fun topReplacementOf(number: Int, record: Record): Map<String, String> {
        return mapOf(
            "%ordinal%" to NumberUtils.ordinalOf(number),
            "%number%" to number.toString(),
            "%player%" to (record.fisher.name ?: "null"),
            "%length%" to record.fish.length.toString(),
            "%fish%" to record.fish.type.name
        )
    }
}