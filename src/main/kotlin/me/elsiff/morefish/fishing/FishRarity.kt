package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.catcheffect.CatchHandler
import org.bukkit.ChatColor

/**
 * Created by elsiff on 2018-12-23.
 */
data class FishRarity(
    val name: String,
    val displayName: String,
    val default: Boolean,
    val probability: Double,
    val color: ChatColor,
    val additionalPrice: Double = 0.0,
    val catchHandlers: Set<CatchHandler>
)