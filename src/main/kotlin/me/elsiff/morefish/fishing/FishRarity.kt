package me.elsiff.morefish.fishing

import org.bukkit.ChatColor

/**
 * Created by elsiff on 2018-12-23.
 */
data class FishRarity(val name: String, val displayName: String, val chance: Double, val color: ChatColor, val feature: Feature) {
    data class Feature(val additionalPrice: Double, val noBroadcast: Boolean, val noDisplay: Boolean, val firework: Boolean)
}