package me.elsiff.morefish.fishing

import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import me.elsiff.morefish.resource.configuration.getFishRarityList
import me.elsiff.morefish.resource.configuration.getFishTypeList
import kotlin.random.Random


/**
 * Created by elsiff on 2018-12-23.
 */
class FishTypeTable : ResourceReceiver {
    private val fishTypeMap = mutableMapOf<FishRarity, MutableList<FishType>>()
    private var defaultRarity: FishRarity? = null

    override fun receiveResource(resources: ResourceBundle) {
        defaultRarity = null
        fishTypeMap.clear()
        resources.fish.getFishRarityList("rarity-list").forEach(this::addRarity)
        fishTypeMap.keys.forEach { rarity ->
            val skullNbtHandler = with(resources.protocolLib) {
                if (hasHooked) skullNbtHandler else null
            }
            resources.fish.getFishTypeList("fish-list", rarity, skullNbtHandler).forEach { type ->
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
        require(!rarity.default || defaultRarity == null) { "Default rarity must be only one" }
        val probabilitySum = fishTypeMap.keys
                .plus(rarity)
                .filter { !it.default }
                .sumByDouble { it.probability }
        check(probabilitySum <= 1.0) { "Sum of rarity probabilities must not be bigger than 1.0" }

        fishTypeMap[rarity] = mutableListOf()
        if (rarity.default)
            defaultRarity = rarity
    }

    fun removeRarity(rarity: FishRarity) = fishTypeMap.remove(rarity)

    fun containsRarity(rarity: FishRarity): Boolean = fishTypeMap.contains(rarity)

    fun addType(fishType: FishType, rarity: FishRarity) {
        val fishTypes = fishTypesOf(rarity)
        fishTypes.add(fishType)
    }

    fun removeType(fishType: FishType, rarity: FishRarity) {
        val fishTypes = fishTypesOf(rarity)
        if (fishType !in fishTypes)
            throw IllegalStateException("Fish Type doesn't exist")
        else
            fishTypes.remove(fishType)
    }

    fun containsType(fishType: FishType, rarity: FishRarity): Boolean {
        return fishTypeMap[rarity]?.contains(fishType) ?: false
    }

    fun pickRandomRarity(): FishRarity {
        val rarities = fishTypeMap.keys
        val randomVal = Random.nextDouble()
        var chanceSum = 0.0
        for (rarity in rarities) {
            if (!rarity.default) {
                chanceSum += rarity.probability
                if (randomVal <= chanceSum) {
                    return rarity
                }
            }
        }
        return defaultRarity ?: throw IllegalStateException("Default rarity doesn't exist")
    }

    fun pickRandomType(): FishType = pickRandomType(pickRandomRarity())

    fun pickRandomType(rarity: FishRarity): FishType {
        val fishTypes = fishTypesOf(rarity)
        return fishTypes.random()
    }

    private fun fishTypesOf(rarity: FishRarity): MutableList<FishType> {
        return fishTypeMap[rarity] ?: throw IllegalStateException("Rarity doesn't exist")
    }
}