package me.elsiff.morefish.fishing.competition

import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-20.
 */
class Prize(
    private val commands: List<String>
) {
    fun giveTo(player: Player) {
        val server = player.server
        for (command in commands) {
            server.dispatchCommand(server.consoleSender, command.replace("@p", player.name))
        }
    }
}