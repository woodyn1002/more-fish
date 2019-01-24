package me.elsiff.morefish.update

import me.elsiff.morefish.configuration.Lang
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created by elsiff on 2019-01-03.
 */
class UpdateNotifierListener(
    private val newVersion: String
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp) {
            for (msg in Lang.formats("new-version").replace("%s" to newVersion).output(event.player)) {
                event.player.sendMessage(msg)
            }
        }
    }
}