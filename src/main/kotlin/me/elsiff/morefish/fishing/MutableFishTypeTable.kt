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
        val probabilitySum = rarities.sumByDouble { it.probability }

        val rarities = keys.toList().filter { !it.default }.sortedBy { it.probability }
        val randomVal = Random.nextDouble()
        for (rarity in rarities) {
            // Weighted selection (probability / probabilitySum <= 1.0)
            if (randomVal <= rarity.probability / probabilitySum) {
                return rarity
            }
        }

        if (rarities.isEmpty()) {
            throw IllegalStateException("Add rarity at least 1")
        }

        // return biggest chance rarity.
        return rarities[rarities.size - 1]
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