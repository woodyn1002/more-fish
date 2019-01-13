package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * Created by elsiff on 2018-12-23.
 */
class MutableFishTypeTable : HashMap<FishRarity, Set<FishType>>(), FishTypeTable {
    override val defaultRarity: FishRarity?
        get() {
            val defaultRarities = rarities.filter { it.default }
            check(defaultRarities.size <= 1) { "Default rarity must be only one" }

            return if (!defaultRarities.isEmpty())
                return defaultRarities[0]
            else
                null
        }

    override val rarities: Set<FishRarity>
        get() = keys.toSet()

    override val types: Set<FishType>
        get() = values.flatten().toSet()

    override fun pickRandomRarity(): FishRarity {
        val probabilitySum = rarities
            .filter { !it.default }
            .sumByDouble { it.probability }
        check(probabilitySum <= 1.0) { "Sum of rarity probabilities must not be bigger than 1.0" }

        val rarities = keys
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

    override fun pickRandomType(rarity: FishRarity): FishType {
        check(contains(rarity)) { "Rarity must be contained in the table" }
        return this[rarity]!!.random()
    }

    override fun pickRandomType(
        caught: Item,
        fisher: Player,
        competition: FishingCompetition,
        rarity: FishRarity
    ): FishType {
        check(contains(rarity)) { "Rarity must be contained in the table" }
        val types = this[rarity]!!.filter { type ->
            type.feature.conditions.all { condition ->
                condition.check(caught, fisher, competition)
            }
        }
        check(types.isNotEmpty()) { "No fish type matches given condition" }
        return types.random()
    }
}