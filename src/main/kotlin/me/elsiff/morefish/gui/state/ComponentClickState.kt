package me.elsiff.morefish.gui.state

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

/**
 * Created by elsiff on 2019-01-05.
 */
data class ComponentClickState(
    val player: Player,
    val cursor: ItemStack?,
    val slot: Int,
    val slotType: InventoryType.SlotType,
    val clickType: ClickType
) : GuiState {
    companion object {
        fun of(event: InventoryClickEvent): ComponentClickState {
            return ComponentClickState(
                player = event.whoClicked as Player,
                cursor = event.cursor ?: null,
                slot = event.rawSlot,
                slotType = event.slotType,
                clickType = event.click
            )
        }
    }
}