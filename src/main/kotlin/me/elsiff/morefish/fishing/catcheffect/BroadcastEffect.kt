package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class BroadcastEffect : CatchHandler {
    override fun play(catcher: Player, fish: Fish) {
        for (player in catcher.world.players) {
            player.sendMessage("${catcher.name} has caught ${fish.length}cm ${fish.type.name}")
        }
    }
}