package me.elsiff.morefish.fishing

import org.bukkit.ChatColor

/**
 * Created by elsiff on 2018-12-23.
 */
data class FishRarity(
        val name: String,
        val displayName: String,
        val default: Boolean,
        val chance: Double,
        val color: ChatColor,
        val feature: Feature
) {
    data class Feature(
            val additionalPrice: Double = 0.0,
            val noBroadcast: Boolean = false,
            val noDisplay: Boolean = false,
            val firework: Boolean = false
    )
}