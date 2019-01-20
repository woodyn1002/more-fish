package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.hooker.WorldGuardHooker
import org.bukkit.entity.Item
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-12.
 */
class WorldGuardRegionCondition(
    private val worldGuardHooker: WorldGuardHooker,
    private val regionId: String
) : FishCondition {
    override fun check(caught: Item, fisher: Player, competition: FishingCompetition): Boolean {
        return worldGuardHooker.containsLocation(regionId, caught.location)
    }
}