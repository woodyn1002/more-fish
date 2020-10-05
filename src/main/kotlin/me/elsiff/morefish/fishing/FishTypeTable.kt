package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-09.
 */
interface FishTypeTable : Map<FishRarity, Set<FishType>> {
    val rarities: Set<FishRarity>
    val types: Set<FishType>

    fun pickRandomRarity(): FishRarity

    fun pickRandomType(rarity: FishRarity = pickRandomRarity()): FishType

    fun pickRandomType(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition,
        rarity: FishRarity = pickRandomRarity()
    ): FishType
}