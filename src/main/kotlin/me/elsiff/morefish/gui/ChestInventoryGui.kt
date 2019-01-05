package me.elsiff.morefish.gui

import org.bukkit.Server

/**
 * Created by elsiff on 2019-01-05.
 */
abstract class ChestInventoryGui(
        server: Server,
        val height: Int,
        val title: String
) : InventoryGui() {
    companion object {
        const val width = 9
    }

    override val inventory = server.createInventory(null, height * width, title)
    protected val minX = 0
    protected val minY = 0
    protected val centerX = width / 2
    protected val centerY = height / 2
    protected val maxX = width - 1
    protected val maxY = height - 1

    override fun slots(): List<Int> {
        return (0 until inventory.size).toList()
    }

    override fun slotOf(x: Int, y: Int): Int {
        require(x in 0..(width - 1)) { "X coordinate is out of range" }
        require(y in 0..(height - 1)) { "Y coordinate is out of range" }
        return (y * width) + x
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
}