package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.condition.Condition
import org.bukkit.inventory.ItemStack
import kotlin.math.floor
import kotlin.random.Random

/**
 * Created by elsiff on 2018-12-20.
 */
data class FishType(
        val name: String,
        val displayName: String,
        val lengthMin: Double,
        val lengthMax: Double,
        val icon: ItemStack,
        val feature: Feature
) {
    data class Feature(
            val skipItemFormat: Boolean = false,
            val commands: List<String> = emptyList(),
            val conditions: List<Condition> = emptyList()
    )

    fun generateFish(): Fish {
        val length = lengthMin + Random.nextDouble() * (lengthMax - lengthMin)
        return Fish(this, floor(length * 10) / 10)
    }
}
