package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.announcement.NoAnnouncement
import me.elsiff.morefish.announcement.PlayerAnnouncement
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-13.
 */
class CatchBroadcaster : AbstractBroadcaster() {
    override val catchMessageFormat: TextFormat
        get() = Lang.format("catch-fish")

    override fun meetBroadcastCondition(catcher: Player, fish: Fish): Boolean {
        return fish.type.catchAnnouncement !is NoAnnouncement
    }

    override fun announcement(fish: Fish): PlayerAnnouncement {
        return fish.type.catchAnnouncement
    }
}