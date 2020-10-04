package me.elsiff.morefish.fishing.competition

import me.elsiff.morefish.util.NumberUtils
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin

/**
 * Created by elsiff on 2019-01-20.
 */
class Prize(
    private val commands: List<String>
) {
    fun giveTo(player: OfflinePlayer, rankNumber: Int, plugin: Plugin) {
        if (!player.isOnline) {
            val ordinal = NumberUtils.ordinalOf(rankNumber)
            plugin.logger.warning("$ordinal fisher ${player.name} isn't online! Contest prizes may not be sent.")
        }

        val server = plugin.server
        player.name?.let {
            for (command in commands) {
                server.dispatchCommand(server.consoleSender, command.replace("@p", it))
            }
        }
    }
}