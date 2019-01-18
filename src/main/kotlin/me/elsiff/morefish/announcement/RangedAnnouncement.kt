package me.elsiff.morefish.announcement

import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-18.
 */
class RangedAnnouncement(
    private val radius: Double
) : PlayerAnnouncement {
    override fun receiversOf(catcher: Player): Collection<Player> =
        catcher.world.players.filter { it.location.distance(catcher.location) <= radius }
}