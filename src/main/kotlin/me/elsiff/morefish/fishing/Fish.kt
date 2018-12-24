package me.elsiff.morefish.fishing

import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2018-12-24.
 */
data class Fish(
        val type: FishType,
        val length: Double
) {
    fun toItemStack(): ItemStack {
        return TODO("Need mechanism for making item stacks")
    }
}