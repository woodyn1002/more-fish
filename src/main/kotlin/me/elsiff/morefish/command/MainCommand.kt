package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.Record
import me.elsiff.morefish.util.NumberUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


/**
 * Created by elsiff on 2018-12-26.
 */
@CommandAlias("morefish|mf|fish")
class MainCommand(
        private val plugin: MoreFish
) : BaseCommand() {
    private val competition = plugin.competition
    private val resources = plugin.resources

    @Default
    @Subcommand("help")
    @CommandPermission("morefish.help")
    fun help(sender: CommandSender) {
        val pluginName = plugin.description.name
        val prefix = "${ChatColor.AQUA}[$pluginName]${ChatColor.RESET} "
        sender.sendMessage(prefix +
                "${ChatColor.DARK_AQUA}> ===== " +
                "${ChatColor.AQUA}${ChatColor.BOLD}$pluginName ${ChatColor.AQUA}v${plugin.description.version}" +
                "${ChatColor.DARK_AQUA} ===== <")
        val label = execCommandLabel
        sender.sendMessage("$prefix/$label help")
        sender.sendMessage("$prefix/$label start [runningTime(sec)]")
        sender.sendMessage("$prefix/$label stop")
        sender.sendMessage("$prefix/$label rewards")
        sender.sendMessage("$prefix/$label clear")
        sender.sendMessage("$prefix/$label reload")
        sender.sendMessage("$prefix/$label top")
        sender.sendMessage("$prefix/$label shop [player]")
    }

    @Subcommand("start")
    @CommandPermission("morefish.admin")
    fun start(sender: CommandSender, args: Array<String>) {
        if (competition.state != FishingCompetition.State.ENABLED) {
            if (args.size == 1) {
                try {
                    val runningTime = args[0].toLong()
                    if (runningTime < 0) {
                        sender.sendMessage(resources.lang.notPositive.formattedEmpty())
                    } else {
                        competition.enableWithTimer(runningTime * 20)
                        sender.sendMessage(resources.lang.contestStartTimer.formatted(mapOf(
                                "%time%" to resources.lang.formatTime(runningTime)
                        )))
                    }
                } catch (e: NumberFormatException) {
                    sender.sendMessage(resources.lang.notNumber.formatted(mapOf("%s" to args[0])))
                }
            } else {
                competition.enable()
                sender.sendMessage(resources.lang.contestStart.formattedEmpty())
            }
        } else {
            sender.sendMessage(resources.lang.alreadyOngoing.formattedEmpty())
        }
    }

    @Subcommand("stop|end")
    @CommandPermission("morefish.admin")
    fun stop(sender: CommandSender) {
        if (competition.state != FishingCompetition.State.DISABLED) {
            competition.disable()
            sender.sendMessage(resources.lang.contestStop.formattedEmpty())
        } else {
            sender.sendMessage(resources.lang.alreadyStopped.formattedEmpty())
        }
    }

    @Subcommand("top|ranking")
    @CommandPermission("morefish.top")
    fun top(sender: CommandSender) {
        if (competition.ranking().isEmpty()) {
            sender.sendMessage(resources.lang.topNoRecord.formattedEmpty())
        } else {
            competition.top(5).forEachIndexed { index, record ->
                val number = index + 1
                sender.sendMessage(resources.lang.topList.formatted(topReplacementOf(number, record)))
            }
        }

        if (sender is Player) {
            competition.getRecordRanked(sender).let {
                val placeholders = topReplacementOf(it.first, it.second)
                sender.sendMessage(resources.lang.topMine.formatted(placeholders))
            }
        }
    }

    private fun topReplacementOf(number: Int, record: Record): Map<String, String> {
        return mapOf(
                "%ordinal%" to NumberUtils.getOrdinal(number),
                "%number%" to number.toString(),
                "%player%" to record.fisher.name,
                "%length%" to record.fish.length.toString(),
                "%fish%" to record.fish.type.name
        )
    }

    @Subcommand("clear")
    @CommandPermission("morefish.admin")
    fun clear(sender: CommandSender) {
        competition.clear()
        sender.sendMessage(resources.lang.clearRecords.formattedEmpty())
    }

    @Subcommand("reload")
    @CommandPermission("morefish.admin")
    fun reload(sender: CommandSender) {
        try {
            plugin.loadAndApplyResources()
            sender.sendMessage(resources.lang.reloadConfig.formattedEmpty())
        } catch (e: Exception) {
            sender.sendMessage(resources.lang.failedToReload.formattedEmpty())
        }
    }
}