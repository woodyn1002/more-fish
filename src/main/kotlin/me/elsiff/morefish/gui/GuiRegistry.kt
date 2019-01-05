package me.elsiff.morefish.gui

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Created by elsiff on 2019-01-05.
 */
class GuiRegistry(
        private val plugin: Plugin
) {
    private val pluginManager = plugin.server.pluginManager
    private val guis = mutableSetOf<Gui>()
    private val listeners = mutableMapOf<Gui, Listener>()

    fun guis(): Set<Gui> {
        return guis
    }

    fun isRegistered(gui: Gui): Boolean {
        return guis.contains(gui)
    }

    fun register(gui: Gui) {
        guis.add(gui)
        gui.createListener(this).apply {
            pluginManager.registerEvents(this, plugin)
            listeners[gui] = this
        }
    }

    fun unregister(gui: Gui) {
        guis.remove(gui)
        HandlerList.unregisterAll(listeners[gui])
        listeners.remove(gui)
    }

    fun clear(closeAll: Boolean = false) {
        if (closeAll) {
            guis.map { gui -> gui.viewers() }.flatten().forEach { it.closeInventory() }
        }
        for (gui in guis()) {
            unregister(gui)
        }
    }

    fun guiOf(player: Player): Gui {
        return guis.find { gui -> gui.containsViewer(player) }
                ?: throw IllegalStateException("Player isn't viewing any gui")
    }

    fun isViewer(player: Player): Boolean {
        return guis.any { gui -> gui.containsViewer(player) }
    }
}