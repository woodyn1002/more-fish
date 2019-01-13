package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-12.
 */
class RainingCondition(
    private val raining: Boolean
) : FishCondition {
    override fun check(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition
    ): Boolean {
        return caught.world.hasStorm() == raining
    }
}