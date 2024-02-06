package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-28.
 */
interface FishCondition {
    fun check(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition
    ): Boolean
}
