package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-13.
 */
class NewFirstBroadcaster(
    private val competition: FishingCompetition
) : AbstractBroadcaster(
    "get-1st",
    "messages.announce-new-1st"
) {
    override fun hasBroadcastCondition(catcher: Player, fish: Fish): Boolean {
        return competition.state == FishingCompetition.State.ENABLED && competition.willBeNewFirst(catcher, fish)
    }
}