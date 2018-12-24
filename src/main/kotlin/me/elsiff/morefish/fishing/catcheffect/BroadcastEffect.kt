package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class BroadcastEffect : CatchEffect {
    override fun play(catcher: Player, fish: Fish) {
        catcher.world.players.forEach {
            it.sendMessage("${catcher.name} has caught ${fish.length}cm ${fish.type.name}")
        }
    }
}