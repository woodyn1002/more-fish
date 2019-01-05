package me.elsiff.morefish.gui

import me.elsiff.morefish.gui.listener.InventoryGuiListener
import me.elsiff.morefish.gui.state.ComponentClickState
import me.elsiff.morefish.gui.state.GuiDragState
import me.elsiff.morefish.gui.state.GuiItemChangeState
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory

/**
 * Created by elsiff on 2019-01-05.
 */
abstract class InventoryGui : Gui {
    protected abstract val inventory: Inventory
    protected val controllableSlots = mutableSetOf<Int>()
    private val viewers = mutableSetOf<Player>()

    override fun showTo(player: Player) {
        require(!viewers.contains(player)) { "Player is already a viewer of this gui" }
        player.openInventory(inventory)
        viewers.add(player)
    }

    abstract fun slots(): List<Int>

    fun controllableSlots(): List<Int> {
        return controllableSlots.toList().sorted()
    }

    fun isControllable(slot: Int): Boolean {
        require(slot >= 0 && slot < inventory.size) { "Slot is out of range" }
        return controllableSlots.contains(slot)
    }

    override fun removeViewer(player: Player) {
        require(viewers.contains(player)) { "Player isn't a viewer of this gui" }
        viewers.remove(player)
    }

    override fun viewers(): Collection<Player> {
        return viewers
    }

    override fun createListener(guiRegistry: GuiRegistry): Listener {
        return InventoryGuiListener(this, guiRegistry)
    }

    fun match(inventory: Inventory): Boolean {
        return this.inventory == inventory
    }

    abstract fun handleItemChange(state: GuiItemChangeState)

    abstract fun handleComponentClick(state: ComponentClickState)

    abstract fun handleDrag(state: GuiDragState)

    protected abstract fun slotOf(x: Int, y: Int): Int

    protected abstract fun slotsOf(x: IntRange, y: Int): List<Int>

    protected abstract fun slotsOf(x: Int, y: IntRange): List<Int>

    protected abstract fun slotsOf(x: IntRange, y: IntRange): List<Int>
}