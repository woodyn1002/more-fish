package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-13.
 */
class CatchBroadcaster : AbstractBroadcaster(
    "catch-fish",
    "messages.announce-catch"
) {
    override fun hasBroadcastCondition(catcher: Player, fish: Fish): Boolean {
        return !fish.type.noDisplay
    }
}