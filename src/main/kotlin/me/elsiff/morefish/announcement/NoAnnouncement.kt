package me.elsiff.morefish.announcement

import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-18.
 */
class NoAnnouncement : PlayerAnnouncement {
    override fun receiversOf(catcher: Player): Collection<Player> =
        emptyList()
}