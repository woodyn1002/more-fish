package me.elsiff.morefish.gui.state

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Created by elsiff on 2019-01-05.
 */
data class GuiCloseState(
    val player: Player
) : GuiState {
    companion object {
        fun of(event: InventoryCloseEvent): GuiCloseState {
            return GuiCloseState(event.player as Player)
        }
    }
}