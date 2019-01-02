package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.Record
import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceProvider
import me.elsiff.morefish.resource.ResourceReceiver
import me.elsiff.morefish.resource.template.TemplateBundle
import me.elsiff.morefish.util.NumberUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile


/**
 * Created by elsiff on 2018-12-26.
 */
@CommandAlias("morefish|mf|fish")
class MainCommand(
        private val pluginInfo: PluginDescriptionFile,
        private val competition: FishingCompetition,
        private val resourceProvider: ResourceProvider
) : BaseCommand(), ResourceReceiver {
    private lateinit var templates: TemplateBundle

    override fun receiveResource(resources: ResourceBundle) {
        templates = resources.templates
    }

    @Default
    @Subcommand("help")
    @CommandPermission("morefish.help")
    fun help(sender: CommandSender) {
        val pluginName = pluginInfo.name
        val prefix = "${ChatColor.AQUA}[$pluginName]${ChatColor.RESET} "
        sender.sendMessage(prefix +
                "${ChatColor.DARK_AQUA}> ===== " +
                "${ChatColor.AQUA}${ChatColor.BOLD}$pluginName ${ChatColor.AQUA}v${pluginInfo.version}" +
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
                        sender.sendMessage(templates.notPositive.formattedEmpty())
                    } else {
                        competition.enableWithTimer(runningTime * 20)
                        sender.sendMessage(templates.contestStartTimer.formatted(mapOf(
                                "%time%" to templates.formatTime(runningTime)
                        )))
                    }
                } catch (e: NumberFormatException) {
                    sender.sendMessage(templates.notNumber.formatted(mapOf("%s" to args[0])))
                }
            } else {
                competition.enable()
                sender.sendMessage(templates.contestStart.formattedEmpty())
            }
        } else {
            sender.sendMessage(templates.alreadyOngoing.formattedEmpty())
        }
    }

    @Subcommand("stop|end")
    @CommandPermission("morefish.admin")
    fun stop(sender: CommandSender) {
        if (competition.state != FishingCompetition.State.DISABLED) {
            competition.disable()
            sender.sendMessage(templates.contestStop.formattedEmpty())
        } else {
            sender.sendMessage(templates.alreadyStopped.formattedEmpty())
        }
    }

    @Subcommand("top|ranking")
    @CommandPermission("morefish.top")
    fun top(sender: CommandSender) {
        if (competition.ranking().isEmpty()) {
            sender.sendMessage(templates.topNoRecord.formattedEmpty())
        } else {
            competition.top(5).forEachIndexed { index, record ->
                val number = index + 1
                sender.sendMessage(templates.topList.formatted(topReplacementOf(number, record)))
            }
        }

        if (sender is Player) {
            competition.getRecordRanked(sender).let {
                val placeholders = topReplacementOf(it.first, it.second)
                sender.sendMessage(templates.topMine.formatted(placeholders))
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
        sender.sendMessage(templates.clearRecords.formattedEmpty())
    }

    @Subcommand("reload")
    @CommandPermission("morefish.admin")
    fun reload(sender: CommandSender) {
        try {
            resourceProvider.provideAll()
            sender.sendMessage(templates.reloadConfig.formattedEmpty())
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(templates.failedToReload.formattedEmpty())
        }
    }
}