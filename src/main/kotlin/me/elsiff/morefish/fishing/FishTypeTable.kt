package me.elsiff.morefish.fishing

import me.elsiff.morefish.protocollib.ProtocolLibHooker
import me.elsiff.morefish.resource.FishResource


/**
 * Created by elsiff on 2018-12-23.
 */
class FishTypeTable {
    private val fishTypeMap = mutableMapOf<FishRarity, MutableList<FishType>>()

    fun load(fishResource: FishResource, protocolLib: ProtocolLibHooker) {
        fishResource.rarityList.forEach(this::addRarity)
        fishTypeMap.keys.forEach { rarity ->
            fishResource.fishList.getFromRarity(rarity, protocolLib).forEach { type ->
                addType(type, rarity)
            }
        }
    }

    fun getRarity(name: String): FishRarity {
        for (rarity in fishTypeMap.keys) {
            if (rarity.name == name) {
                return rarity
            }
        }
        throw IllegalStateException("No such fish rarity found")
    }

    fun getType(name: String): FishType {
        for (type in fishTypeMap.values.flatten()) {
            if (type.name == name) {
                return type
            }
        }
        throw IllegalStateException("No such fish type found")
    }

    fun rarities(): Set<FishRarity> = fishTypeMap.keys.toSet()

    fun types(): Set<FishType> = fishTypeMap.values.flatten().toSet()

    fun addRarity(rarity: FishRarity) {
        require(!rarity.default || !::defaultRarity.isInitialized) { "Default rarity must be only one" }
        val probabilitySum = fishTypeMap.keys
                .plus(rarity)
                .filter { !it.default }
                .sumByDouble { it.probability }
        check(probabilitySum <= 1.0) { "Sum of rarity probabilities must not be bigger than 1.0" }

        fishTypeMap[rarity] = mutableListOf()
    }

    fun removeRarity(rarity: FishRarity) = fishTypeMap.remove(rarity)

    fun containsRarity(rarity: FishRarity): Boolean = fishTypeMap.contains(rarity)

    fun addType(fishType: FishType, rarity: FishRarity) {
        val fishTypes = fishTypeMap[rarity] ?: throw IllegalStateException("Rarity doesn't exist")
        fishTypes.add(fishType)
    }

    fun removeType(fishType: FishType, rarity: FishRarity) {
        val fishTypes = fishTypeMap[rarity] ?: throw IllegalStateException("Rarity doesn't exist")
        if (fishType !in fishTypes)
            throw IllegalStateException("Fish Type doesn't exist")
        else
            fishTypes.remove(fishType)
    }

    fun containsType(fishType: FishType, rarity: FishRarity): Boolean {
        return fishTypeMap[rarity]?.contains(fishType) ?: false
    }

    fun pickRandomRarity(): FishRarity = fishTypeMap.keys.random()

    fun pickRandomType(): FishType = pickRandomType(pickRandomRarity())

    fun pickRandomType(rarity: FishRarity): FishType {
        val fishTypes = fishTypeMap[rarity] ?: throw IllegalStateException("Rarity doesn't exist")
        return fishTypes.random()
    }
}