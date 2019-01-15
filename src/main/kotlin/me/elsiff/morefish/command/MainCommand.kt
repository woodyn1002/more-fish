package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.Record
import me.elsiff.morefish.shop.FishShop
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
    private val moreFish: MoreFish,
    private val competition: FishingCompetition,
    private val fishShop: FishShop
) : BaseCommand() {
    private val pluginInfo: PluginDescriptionFile = moreFish.description

    @Default
    @Subcommand("help")
    @CommandPermission("morefish.help")
    fun help(sender: CommandSender) {
        val pluginName = pluginInfo.name
        val prefix = "${ChatColor.AQUA}[$pluginName]${ChatColor.RESET} "
        sender.sendMessage(
            prefix +
                    "${ChatColor.DARK_AQUA}> ===== " +
                    "${ChatColor.AQUA}${ChatColor.BOLD}$pluginName ${ChatColor.AQUA}v${pluginInfo.version}" +
                    "${ChatColor.DARK_AQUA} ===== <"
        )
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
        if (!competition.isEnabled()) {
            if (args.size == 1) {
                try {
                    val runningTime = args[0].toLong()
                    if (runningTime < 0) {
                        sender.sendMessage(Lang.text("not-positive"))
                    } else {
                        competition.enableWithTimer(runningTime * 20)

                        val msg = Lang.format("contest-start-timer")
                            .replace("%time%" to Lang.time(runningTime)).output
                        sender.sendMessage(msg)
                    }
                } catch (e: NumberFormatException) {
                    val msg = Lang.format("not-number").replace("%s" to args[0]).output
                    sender.sendMessage(msg)
                }
            } else {
                competition.enable()
                sender.sendMessage(Lang.text("contest-start"))
            }
        } else {
            sender.sendMessage(Lang.text("already-ongoing"))
        }
    }

    @Subcommand("stop|end")
    @CommandPermission("morefish.admin")
    fun stop(sender: CommandSender) {
        if (!competition.isDisabled()) {
            competition.disable()
            sender.sendMessage(Lang.text("contest-shop"))
        } else {
            sender.sendMessage(Lang.text("already-stopped"))
        }
    }

    @Subcommand("top|ranking")
    @CommandPermission("morefish.top")
    fun top(sender: CommandSender) {
        if (competition.ranking.isEmpty()) {
            sender.sendMessage(Lang.text("top-no-record"))
        } else {
            competition.top(5).forEachIndexed { index, record ->
                val number = index + 1
                val msg = Lang.format("top-list").replace(topReplacementOf(number, record)).output
                sender.sendMessage(msg)
            }
        }

        if (sender is Player) {
            if (!competition.containsContestant(sender)) {
                sender.sendMessage(Lang.text("top-mine-no-record"))
            } else {
                competition.rankedRecordOf(sender).let {
                    val msg = Lang.format("top-mine").replace(topReplacementOf(it.first, it.second)).output
                    sender.sendMessage(msg)
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

    @Subcommand("clear")
    @CommandPermission("morefish.admin")
    fun clear(sender: CommandSender) {
        competition.clear()
        sender.sendMessage(Lang.text("clear-records"))
    }

    @Subcommand("reload")
    @CommandPermission("morefish.admin")
    fun reload(sender: CommandSender) {
        try {
            moreFish.applyConfig()
            sender.sendMessage(Lang.text("reload-config"))
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(Lang.text("failed-to-reload"))
        }
    }

    @Subcommand("shop")
    @CommandPermission("morefish.shop")
    fun shop(sender: CommandSender, args: Array<String>) {
        val guiUser: Player = if (args.size == 1) {
            if (sender.hasPermission("morefish.admin")) {
                sender.sendMessage(Lang.text("no-permission"))
                return
            }

            val target = sender.server.getPlayerExact(args[0]) ?: null
            if (target == null) {
                val msg = Lang.format("player-not-found").replace("%s" to args[0]).output
                sender.sendMessage(msg)
                return
            } else {
                target
            }
        } else {
            if (sender !is Player) {
                sender.sendMessage(Lang.text("in-game-command"))
                return
            }
            sender
        }

        if (!fishShop.enabled) {
            sender.sendMessage(Lang.text("shop-disabled"))
        } else {
            fishShop.openGuiTo(guiUser)

            if (guiUser != sender) {
                val msg = Lang.format("forced-player-to-shop").replace("%s" to guiUser.name).output
                sender.sendMessage(msg)
            }
        }
    }
}