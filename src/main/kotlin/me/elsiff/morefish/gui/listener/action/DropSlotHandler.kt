package me.elsiff.morefish.gui.listener.action

import me.elsiff.morefish.gui.InventoryGui
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by elsiff on 2019-01-28.
 */
class DropSlotHandler : InventoryActionHandler {
    override val handlingActions: Collection<InventoryAction> = setOf(
        InventoryAction.DROP_ALL_SLOT,
        InventoryAction.DROP_ONE_SLOT
    )

    override fun handle(event: InventoryClickEvent, gui: InventoryGui) {
        event.isCancelled = true
    }
}