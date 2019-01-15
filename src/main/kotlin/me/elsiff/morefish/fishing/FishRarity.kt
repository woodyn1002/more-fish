package me.elsiff.morefish.fishing

import me.elsiff.morefish.fishing.catchhandler.CatchHandler
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
    val catchHandlers: List<CatchHandler>,
    val hasNotFishItemFormat: Boolean = false,
    val noBroadcast: Boolean = false,
    val noDisplay: Boolean = false,
    val hasCatchFirework: Boolean = false,
    val additionalPrice: Double = 0.0
)