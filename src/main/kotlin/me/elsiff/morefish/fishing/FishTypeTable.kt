package me.elsiff.morefish.fishing

/**
 * Created by elsiff on 2019-01-09.
 */
interface FishTypeTable : Map<FishRarity, Set<FishType>> {
    val defaultRarity: FishRarity?
    val rarities: Set<FishRarity>
    val types: Set<FishType>

    fun pickRandomRarity(): FishRarity

    fun pickRandomType(rarity: FishRarity = pickRandomRarity()): FishType
}