package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-13.
 */
class CatchCommandExecutor(
    private val commands: List<String>
) : CatchHandler {
    override fun handle(catcher: Player, fish: Fish) {
        val server = catcher.server
        for (command in commands) {
            server.dispatchCommand(server.consoleSender, command.replace("@p", catcher.name))
        }
    }
}
