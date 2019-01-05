package me.elsiff.morefish.gui.listener.action

import me.elsiff.morefish.gui.InventoryGui
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by elsiff on 2018-08-22.
 */
interface InventoryActionHandler {
    val handlingActions: Collection<InventoryAction>

    fun handle(event: InventoryClickEvent, gui: InventoryGui)
}