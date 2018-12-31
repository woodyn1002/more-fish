package me.elsiff.morefish

import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2018-12-31.
 */
interface PluginHooker {
    val pluginName: String
    var hasHooked: Boolean

    fun canHook(pluginManager: PluginManager) = pluginManager.isPluginEnabled(pluginName)

    fun hook(pluginManager: PluginManager)

    fun hookIfEnabled(pluginManager: PluginManager) {
        if (canHook(pluginManager)) {
            hook(pluginManager)
        }
    }

    companion object {
        fun checkHooked(hooker: PluginHooker) {
            check(hooker.hasHooked) { "${hooker.pluginName} must be hooked" }
        }
    }
}