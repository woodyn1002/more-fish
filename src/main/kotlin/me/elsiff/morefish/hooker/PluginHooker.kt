package me.elsiff.morefish.hooker

import org.bukkit.Server
import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2018-12-31.
 */
interface PluginHooker {
    val pluginName: String
    var hasHooked: Boolean

    fun canHook(pluginManager: PluginManager) = pluginManager.isPluginEnabled(pluginName)

    fun hook(server: Server)

    fun hookIfEnabled(server: Server) {
        if (canHook(server.pluginManager)) {
            hook(server)
        }
    }

    companion object {
        fun checkEnabled(hooker: PluginHooker, pluginManager: PluginManager) {
            check(hooker.canHook(pluginManager)) { "${hooker.pluginName} must be enabled" }
        }

        fun checkHooked(hooker: PluginHooker) {
            check(hooker.hasHooked) { "${hooker.pluginName} must be hooked" }
        }
    }
}