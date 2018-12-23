package me.elsiff.morefish.fishing

import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2018-12-20.
 */
data class FishType(val name: String, val displayName: String, val lengthMin: Double, val lengthMax: Double,
                    val icon: ItemStack, val feature: Feature) {
    data class Feature(val skipItemFormat: Boolean = false, val commands: List<String> = emptyList())
}
