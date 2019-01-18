package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.ConfigurationSectionAccessor
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.util.NumberUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/**
 * Created by elsiff on 2019-01-18.
 */
class FishingCompetitionHost(
    private val plugin: Plugin,
    val competition: FishingCompetition
) {
    private var timerTask: BukkitTask? = null

    private val msgConfig: ConfigurationSectionAccessor
        get() = Config.standard["messages"]

    fun openCompetition() {
        competition.enable()

        if (msgConfig.boolean("broadcast-start")) {
            plugin.server.broadcastMessage(Lang.text("contest-start"))
        }
    }

    fun openCompetitionFor(tick: Long) {
        competition.enable()
        timerTask = plugin.server.scheduler.runTaskLater(plugin, competition::disable, tick)

        if (msgConfig.boolean("broadcast-start")) {
            val msg = Lang.format("contest-start-timer")
                .replace("%time%" to Lang.time(tick * 20))
                .output
            plugin.server.broadcastMessage(msg)
        }
    }

    fun closeCompetition() {
        competition.disable()
        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null
        }

        if (msgConfig.boolean("broadcast-stop")) {
            plugin.server.broadcastMessage(Lang.text("contest-stop"))

            if (msgConfig.boolean("show-top-on-ending")) {
                for (player in plugin.server.onlinePlayers) {
                    informAboutRanking(player)
                }
            }
        }
        competition.clearRecords()
    }

    fun informAboutRanking(receiver: CommandSender) {
        if (competition.ranking.isEmpty()) {
            receiver.sendMessage(Lang.text("top-no-record"))
        } else {
            competition.top(5).forEachIndexed { index, record ->
                val number = index + 1
                val msg = Lang.format("top-list").replace(topReplacementOf(number, record)).output
                receiver.sendMessage(msg)
            }

            if (receiver is Player) {
                if (!competition.containsContestant(receiver)) {
                    receiver.sendMessage(Lang.text("top-mine-no-record"))
                } else {
                    competition.rankedRecordOf(receiver).let {
                        val msg = Lang.format("top-mine").replace(topReplacementOf(it.first, it.second)).output
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
            "%player%" to record.fisher.name,
            "%length%" to record.fish.length.toString(),
            "%fish%" to record.fish.type.name
        )
    }
}