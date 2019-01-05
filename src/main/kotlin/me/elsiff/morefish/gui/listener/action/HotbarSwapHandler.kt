package me.elsiff.morefish.gui.listener.action

import me.elsiff.morefish.gui.InventoryGui
import me.elsiff.morefish.gui.state.GuiItemChangeState
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by elsiff on 2018-08-22.
 */
class HotbarSwapHandler : InventoryActionHandler {
    override val handlingActions = setOf(
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD
    )

    override fun handle(event: InventoryClickEvent, gui: InventoryGui) {
        if (event.clickedInventory == event.view.topInventory && !gui.isControllable(event.rawSlot)) {
            event.isCancelled = true
        } else {
            gui.handleItemChange(GuiItemChangeState.of(event))
        }
    }
}