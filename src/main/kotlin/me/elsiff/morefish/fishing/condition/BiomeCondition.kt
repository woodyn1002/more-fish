package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.block.Biome
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class BiomeCondition(
    private val biome: Biome
) : FishCondition {
    override fun check(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition
    ): Boolean {
        val x = caught.location.x.toInt()
        val z = caught.location.z.toInt()
        return caught.world.getBiome(x, z) == biome
    }
}