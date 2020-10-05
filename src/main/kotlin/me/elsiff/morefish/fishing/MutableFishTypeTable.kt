package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * Created by elsiff on 2018-12-23.
 */
class MutableFishTypeTable : HashMap<FishRarity, Set<FishType>>(), FishTypeTable {

    override val rarities: Set<FishRarity>
        get() = keys.toSet()

    override val types: Set<FishType>
        get() = values.flatten().toSet()

    override fun pickRandomRarity(): FishRarity {
        val completeWeight = rarities.sumByDouble { it.probability }
        val random = Random.nextDouble() * completeWeight
        var countWeight = 0.0
        for (rarity in keys) {
            countWeight += rarity.probability
            if (countWeight >= random) return rarity
        }

        // Should never be shown. If by any chance reached here, returns biggest chance rarity.
        return rarities.maxByOrNull { it.probability } ?: throw IllegalStateException("Add rarity at least 1")
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
            type.conditions.all { condition ->
                condition.check(caught, fisher, competition)
            }
        }
        check(types.isNotEmpty()) { "No fish type matches given condition" }
        return types.random()
    }
}