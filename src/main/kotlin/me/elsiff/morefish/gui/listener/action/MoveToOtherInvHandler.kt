package me.elsiff.morefish.gui.listener.action

import me.elsiff.morefish.gui.InventoryGui
import me.elsiff.morefish.gui.state.GuiItemChangeState
import me.elsiff.morefish.util.InventoryUtils
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by elsiff on 2018-08-22.
 */
class MoveToOtherInvHandler : InventoryActionHandler {
    override val handlingActions = setOf(InventoryAction.MOVE_TO_OTHER_INVENTORY)

    override fun handle(event: InventoryClickEvent, gui: InventoryGui) {

        val topInv = event.view.topInventory
        val bottomInv = event.view.bottomInventory

        if (event.clickedInventory == topInv) {
            if (gui.isControllable(event.rawSlot)) {
                gui.handleItemChange(GuiItemChangeState.of(event))
            } else {
                event.isCancelled = true
            }
        } else if (event.clickedInventory == bottomInv) {
            event.isCancelled = true
            InventoryUtils.deliverTo(topInv, event.currentItem, gui.controllableSlots())
            gui.handleItemChange(GuiItemChangeState.of(event))
        }
    }
}