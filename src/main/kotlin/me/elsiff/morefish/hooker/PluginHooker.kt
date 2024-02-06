package me.elsiff.morefish.hooker

import me.elsiff.morefish.MoreFish
import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2018-12-31.
 */
interface PluginHooker {
    val pluginName: String
    var hasHooked: Boolean

    fun canHook(pluginManager: PluginManager) = pluginManager.isPluginEnabled(pluginName)

    fun hook(plugin: MoreFish)

    fun hookIfEnabled(plugin: MoreFish) {
        if (canHook(plugin.server.pluginManager)) {
            hook(plugin)
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
