package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.competition.Record
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
class CompetitionRecordAdder(
    private val competition: FishingCompetition
) : CatchHandler {
    override fun handle(catcher: Player, fish: Fish) {
        if (competition.state == FishingCompetition.State.ENABLED) {
            competition.putRecord(Record(catcher, fish))
        }
    }
}