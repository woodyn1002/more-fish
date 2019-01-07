package me.elsiff.morefish.gui.listener

import me.elsiff.morefish.gui.GuiRegistry
import me.elsiff.morefish.gui.InventoryGui
import me.elsiff.morefish.gui.listener.action.*
import me.elsiff.morefish.gui.state.GuiCloseState
import me.elsiff.morefish.gui.state.GuiDragState
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * Created by elsiff on 2019-01-05.
 */
class InventoryGuiListener(
    private val gui: InventoryGui,
    private val guiRegistry: GuiRegistry
) : Listener {
    @EventHandler
    fun onClickInventory(event: InventoryClickEvent) {
        if (gui.match(event.inventory)) {
            checkIsPlayer(event.whoClicked)

            val handler = actionHandlers.find { it.handlingActions.contains(event.action) }
            handler?.handle(event, gui)
        }
    }

    @EventHandler
    fun onDragInventory(event: InventoryDragEvent) {
        if (gui.match(event.inventory)) {
            checkIsPlayer(event.whoClicked)

            if (event.rawSlots.any { slot -> slot < event.view.topInventory.size && !gui.isControllable(slot) }) {
                event.isCancelled = true
            } else {
                gui.handleDrag(GuiDragState.of(event))
            }
        }
    }

    @EventHandler
    fun onCloseInventory(event: InventoryCloseEvent) {
        if (gui.match(event.inventory)) {
            checkIsPlayer(event.player)
            val player = event.player as Player

            gui.handleClose(GuiCloseState.of(event))
            gui.removeViewer(player)

            if (gui.viewers.isEmpty()) {
                guiRegistry.unregister(gui)
            }
        }
    }

    private fun checkIsPlayer(humanEntity: HumanEntity) {
        check(humanEntity is Player) { "Human entity who handles a gui must be a player" }
    }

    companion object {
        private val actionHandlers: Set<InventoryActionHandler> = setOf(
            CollectToCursorHandler(),
            HotbarSwapHandler(),
            MoveToOtherInvHandler(),
            StandardClickHandler()
        )
    }
}