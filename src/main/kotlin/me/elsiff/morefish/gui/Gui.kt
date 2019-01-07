package me.elsiff.morefish.gui

import me.elsiff.morefish.gui.state.GuiCloseState
import me.elsiff.morefish.gui.state.GuiOpenState
import org.bukkit.entity.Player
import org.bukkit.event.Listener

/**
 * Created by elsiff on 2019-01-05.
 */
interface Gui {
    val viewers: Collection<Player>

    fun showTo(player: Player)

    fun removeViewer(player: Player)

    fun containsViewer(player: Player): Boolean = viewers.contains(player)

    fun createListener(guiRegistry: GuiRegistry): Listener

    fun handleOpen(state: GuiOpenState) {
        // Nothing in default
    }

    fun handleClose(state: GuiCloseState) {
        // Nothing in default
    }
}