package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.announcement.PlayerAnnouncement
import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-13.
 */
class NewFirstBroadcaster(
    private val competition: FishingCompetition
) : AbstractBroadcaster() {
    override val catchMessageFormat: TextFormat
        get() = Lang.format("get-1st")

    override fun meetBroadcastCondition(catcher: Player, fish: Fish): Boolean {
        return competition.isEnabled() && competition.willBeNewFirst(catcher, fish)
    }

    override fun announcement(fish: Fish): PlayerAnnouncement {
        return Config.newFirstAnnouncement
    }
}