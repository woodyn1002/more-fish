package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.Record
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class CompetitionEffect(
    private val competition: FishingCompetition
) : CatchEffect {
    override fun play(catcher: Player, fish: Fish) {
        if (competition.state == FishingCompetition.State.ENABLED) {
            competition.putRecord(Record(catcher, fish))
        }
    }
}