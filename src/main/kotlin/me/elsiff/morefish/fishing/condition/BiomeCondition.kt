package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.block.Biome
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class BiomeCondition(
    private val biomes: Collection<Biome>
) : FishCondition {
    override fun check(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition
    ): Boolean {
        val x = caught.location.blockX
        val y = caught.location.blockY
        val z = caught.location.blockZ
        return caught.world.getBiome(x, y, z) in biomes
    }
}