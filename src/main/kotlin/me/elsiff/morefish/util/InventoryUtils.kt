package me.elsiff.morefish.util

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.min

/**
 * Created by elsiff on 2019-01-06.
 */
object InventoryUtils {
    fun emptyStack(): ItemStack = ItemStack(Material.AIR)

    fun deliverTo(inventory: Inventory, delivery: ItemStack, acceptableSlots: List<Int> = inventory.slots()) {
        val contents = acceptableSlots.mapNotNull { slot -> inventory.getItem(slot) }
        for (invItem in contents.filter { it.isSimilar(delivery) }) {
            val givingAmount = min(delivery.amount, invItem.maxStackSize - invItem.amount)
            invItem.amount = invItem.amount + givingAmount
            delivery.amount = delivery.amount - givingAmount

            if (delivery.amount == 0)
                break
        }

        for (emptySlot in acceptableSlots.filter { slot -> inventory.isEmptyAt(slot) }) {
            val placingAmount = min(delivery.amount, delivery.maxStackSize)
            inventory.setItem(emptySlot, delivery.clone().apply { amount = placingAmount })
            delivery.amount = delivery.amount - placingAmount
        }
    }
}
