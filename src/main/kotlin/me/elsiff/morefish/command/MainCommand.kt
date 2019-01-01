package me.elsiff.morefish.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.fishing.competition.FishingCompetition
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
    fun help(player: Player, args: Array<String>) {
        player.sendMessage("Hello World!")
    }

    @Subcommand("start")
    fun start(player: Player, args: Array<String>) {
        if (competition.state != FishingCompetition.State.ENABLED) {
            if (args.size > 1) {
                val runningTime = args[1].toInt()
                player.sendMessage("Fish competition has been enabled with $runningTime seconds")
            } else {
                competition.enable()
                player.sendMessage("Fish competition has been enabled")
            }
        } else {
            player.sendMessage("Fish competition is already enabled")
        }
    }

    @Subcommand("stop|end")
    fun stop(player: Player, args: Array<String>) {
        if (competition.state != FishingCompetition.State.DISABLED) {
            competition.disable()
            player.sendMessage("Fish competition has been disabled")
        } else {
            player.sendMessage("Fish competition is already disabled")
        }
    }

    @Subcommand("top|ranking")
    fun top(player: Player, args: Array<String>) {
        competition.top(5).forEachIndexed { index, record ->
            player.sendMessage("[$index] ${record.fisher.name}, ${record.fish.length}cm")
        }
    }

    @Subcommand("clear")
    fun clear(player: Player, args: Array<String>) {
        if (competition.state != FishingCompetition.State.ENABLED) {
            competition.clear()
            player.sendMessage("Fish competition has been cleared")
        } else {
            player.sendMessage("Fish competition isn't ongoing now")
        }
    }

    @Subcommand("reload")
    fun reload(player: Player, args: Array<String>) {
        plugin.loadAndApplyResources()
        player.sendMessage(resources.lang.reloadConfig)
    }
}