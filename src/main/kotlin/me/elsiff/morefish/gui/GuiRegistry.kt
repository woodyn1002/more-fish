package me.elsiff.morefish.gui

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2019-01-05.
 */
class GuiRegistry(
    private val plugin: Plugin
) {
    private val pluginManager: PluginManager = plugin.server.pluginManager
    private val _guis: MutableSet<Gui> = mutableSetOf()
    val guis: Set<Gui>
        get() = _guis
    private val listeners: MutableMap<Gui, Listener> = mutableMapOf()

    fun isRegistered(gui: Gui): Boolean {
        return _guis.contains(gui)
    }

    fun register(gui: Gui) {
        _guis.add(gui)
        gui.createListener(this).apply {
            pluginManager.registerEvents(this, plugin)
            listeners[gui] = this
        }
    }

    fun unregister(gui: Gui) {
        _guis.remove(gui)
        HandlerList.unregisterAll(listeners[gui])
        listeners.remove(gui)
    }

    fun clear(closeAll: Boolean = false) {
        if (closeAll) {
            for (viewer in _guis.map { gui -> gui.viewers }.flatten()) {
                viewer.closeInventory()
            }
        }
        for (gui in _guis) {
            unregister(gui)
        }
    }

    fun guiOf(player: Player): Gui {
        return _guis.find { gui -> gui.containsViewer(player) }
            ?: throw IllegalStateException("Player isn't viewing any gui")
    }

    fun isViewer(player: Player): Boolean {
        return _guis.any { gui -> gui.containsViewer(player) }
    }
}