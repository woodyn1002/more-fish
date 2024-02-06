package me.elsiff.morefish.util

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2019-01-07.
 */
fun Inventory.isEmptyAt(slot: Int): Boolean {
    val item = getItem(slot)
    return item == null || item.type == Material.AIR
}

fun Inventory.itemAt(slot: Int): ItemStack {
    return getItem(slot) ?: ItemStack(Material.AIR)
}

fun Inventory.slots(): List<Int> {
    return (0 until size).toList()
}
