package me.elsiff.morefish.gui.state

import org.bukkit.event.inventory.DragType
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2019-01-05.
 */
data class GuiDragState(
        val cursor: ItemStack?,
        val slots: Set<Int>,
        val newItems: Map<Int, ItemStack>,
        val oldCursor: ItemStack,
        val dragType: DragType
) : GuiState {
    companion object {
        fun of(event: InventoryDragEvent): GuiDragState {
            return GuiDragState(
                    cursor = event.cursor ?: null,
                    slots = event.rawSlots,
                    newItems = event.newItems,
                    oldCursor = event.oldCursor,
                    dragType = event.type
            )
        }
    }
}