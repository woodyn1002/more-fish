package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.FishingCompetitionHost
import me.elsiff.morefish.shop.FishShop
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
    private val competitionHost: FishingCompetitionHost,
    private val fishShop: FishShop
) : BaseCommand() {
    private val pluginInfo: PluginDescriptionFile = moreFish.description
    private val competition: FishingCompetition = competitionHost.competition

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
        sender.sendMessage("$prefix/$label begin [runningTime(sec)]")
        sender.sendMessage("$prefix/$label suspend")
        sender.sendMessage("$prefix/$label end")
        sender.sendMessage("$prefix/$label rewards")
        sender.sendMessage("$prefix/$label clear")
        sender.sendMessage("$prefix/$label reload")
        sender.sendMessage("$prefix/$label top")
        sender.sendMessage("$prefix/$label shop [player]")
    }

    @Subcommand("begin|start")
    @CommandPermission("morefish.admin")
    fun begin(sender: CommandSender, args: Array<String>) {
        if (!competition.isEnabled()) {
            if (args.size == 1) {
                try {
                    val runningTime = args[0].toLong()
                    if (runningTime < 0) {
                        sender.sendMessage(Lang.text("not-positive"))
                    } else {
                        competitionHost.openCompetitionFor(runningTime * 20)

                        if (!Config.standard.boolean("messages.broadcast-start")) {
                            val msg = Lang.format("contest-start-timer")
                                .replace("%time%" to Lang.time(runningTime)).output()
                            sender.sendMessage(msg)
                        }
                    }
                } catch (e: NumberFormatException) {
                    val msg = Lang.format("not-number").replace("%s" to args[0]).output()
                    sender.sendMessage(msg)
                }
            } else {
                competitionHost.openCompetition()

                if (!Config.standard.boolean("messages.broadcast-start")) {
                    sender.sendMessage(Lang.text("contest-start"))
                }
            }
        } else {
            sender.sendMessage(Lang.text("already-ongoing"))
        }
    }

    @Subcommand("suspend")
    @CommandPermission("morefish.admin")
    fun suspend(sender: CommandSender) {
        if (!competition.isDisabled()) {
            competitionHost.closeCompetition(suspend = true)

            if (!Config.standard.boolean("messages.broadcast-stop")) {
                sender.sendMessage(Lang.text("contest-stop"))
            }
        } else {
            sender.sendMessage(Lang.text("already-stopped"))
        }
    }

    @Subcommand("end")
    @CommandPermission("morefish.admin")
    fun end(sender: CommandSender) {
        if (!competition.isDisabled()) {
            competitionHost.closeCompetition()

            if (!Config.standard.boolean("messages.broadcast-stop")) {
                sender.sendMessage(Lang.text("contest-stop"))
            }
        } else {
            sender.sendMessage(Lang.text("already-stopped"))
        }
    }

    @Subcommand("top|ranking")
    @CommandPermission("morefish.top")
    fun top(sender: CommandSender) {
        competitionHost.informAboutRanking(sender)
    }

    @Subcommand("clear")
    @CommandPermission("morefish.admin")
    fun clear(sender: CommandSender) {
        competition.clearRecords()
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
    fun shop(sender: CommandSender, args: Array<String>) {
        val guiUser: Player = if (args.size == 1) {
            if (!sender.hasPermission("morefish.admin")) {
                sender.sendMessage(Lang.text("no-permission"))
                return
            }

            val target = sender.server.getPlayerExact(args[0]) ?: null
            if (target == null) {
                val msg = Lang.format("player-not-found").replace("%s" to args[0]).output()
                sender.sendMessage(msg)
                return
            } else {
                target
            }
        } else {
            if (!sender.hasPermission("morefish.shop")) {
                sender.sendMessage(Lang.text("no-permission"))
                return
            }
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
                val msg = Lang.format("forced-player-to-shop").replace("%s" to guiUser.name).output()
                sender.sendMessage(msg)
            }
        }
    }
}