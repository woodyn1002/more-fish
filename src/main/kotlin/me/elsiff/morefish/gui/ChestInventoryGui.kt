package me.elsiff.morefish.gui

import org.bukkit.Server
import org.bukkit.inventory.Inventory

/**
 * Created by elsiff on 2019-01-05.
 */
abstract class ChestInventoryGui(
    server: Server,
    val height: Int,
    val title: String
) : InventoryGui() {
    override val inventory: Inventory = server.createInventory(null, height * WIDTH, title)!!
    override val slots: List<Int>
        get() = (0 until inventory.size).toList()
    protected val minX = 0
    protected val minY = 0
    protected val centerX: Int = WIDTH / 2
    protected val centerY: Int = height / 2
    protected val maxX: Int = WIDTH - 1
    protected val maxY: Int = height - 1

    override fun slotOf(x: Int, y: Int): Int {
        require(x in 0..(WIDTH - 1)) { "X coordinate is out of range" }
        require(y in 0..(height - 1)) { "Y coordinate is out of range" }
        return (y * WIDTH) + x
    }

    override fun slotsOf(x: IntRange, y: Int): List<Int> {
        return x.map { slotOf(it, y) }
    }

    override fun slotsOf(x: Int, y: IntRange): List<Int> {
        return y.map { slotOf(x, it) }
    }

    override fun slotsOf(x: IntRange, y: IntRange): List<Int> {
        return x.map { slotsOf(it, y) }.flatten()
    }

    companion object {
        const val WIDTH = 9
    }
}