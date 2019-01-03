package me.elsiff.morefish.protocollib

import me.elsiff.morefish.PluginHooker
import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2018-12-31.
 */
class ProtocolLibHooker: PluginHooker {
    lateinit var skullNbtHandler: SkullNbtHandler
    override val pluginName = "ProtocolLib"
    override var hasHooked = false

    override fun hook(pluginManager: PluginManager) {
        skullNbtHandler = SkullNbtHandler()
        hasHooked = true
    }
}