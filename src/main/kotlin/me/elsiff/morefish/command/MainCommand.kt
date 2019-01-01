package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.command.CommandSender

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
    fun help(sender: CommandSender, args: Array<String>) {
        sender.sendMessage("Hello World!")
    }

    @Subcommand("start")
    fun start(sender: CommandSender, args: Array<String>) {
        if (competition.state != FishingCompetition.State.ENABLED) {
            if (args.size > 1) {
                val runningTime = args[1].toInt()
                sender.sendMessage("Fish competition has been enabled with $runningTime seconds")
            } else {
                competition.enable()
                sender.sendMessage("Fish competition has been enabled")
            }
        } else {
            sender.sendMessage("Fish competition is already enabled")
        }
    }

    @Subcommand("stop|end")
    fun stop(sender: CommandSender, args: Array<String>) {
        if (competition.state != FishingCompetition.State.DISABLED) {
            competition.disable()
            sender.sendMessage("Fish competition has been disabled")
        } else {
            sender.sendMessage("Fish competition is already disabled")
        }
    }

    @Subcommand("top|ranking")
    fun top(sender: CommandSender, args: Array<String>) {
        competition.top(5).forEachIndexed { index, record ->
            sender.sendMessage("[$index] ${record.fisher.name}, ${record.fish.length}cm")
        }
    }

    @Subcommand("clear")
    fun clear(sender: CommandSender, args: Array<String>) {
        if (competition.state != FishingCompetition.State.ENABLED) {
            competition.clear()
            sender.sendMessage("Fish competition has been cleared")
        } else {
            sender.sendMessage("Fish competition isn't ongoing now")
        }
    }

    @Subcommand("reload")
    fun reload(sender: CommandSender, args: Array<String>) {
        plugin.loadAndApplyResources()
        sender.sendMessage(resources.lang.reloadConfig.formattedEmpty())
    }
}