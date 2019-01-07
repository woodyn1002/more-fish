package me.elsiff.morefish.gui.listener.action

import me.elsiff.morefish.gui.InventoryGui
import me.elsiff.morefish.util.itemAt
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by elsiff on 2018-08-22.
 */
class CollectToCursorHandler : InventoryActionHandler {
    override val handlingActions: Set<InventoryAction> = setOf(InventoryAction.COLLECT_TO_CURSOR)

    override fun handle(event: InventoryClickEvent, gui: InventoryGui) {
        val anySimilarToCursor = gui.slots.minus(gui.controllableSlots()).any { slot ->
            val itemStack = event.view.topInventory.itemAt(slot)
            itemStack.isSimilar(event.cursor)
        }
        if (anySimilarToCursor) {
            event.isCancelled = true
        }
    }
}