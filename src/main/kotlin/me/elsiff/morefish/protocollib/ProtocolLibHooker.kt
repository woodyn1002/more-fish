package me.elsiff.morefish.protocollib

import me.elsiff.morefish.PluginHooker
import org.bukkit.Server

/**
 * Created by elsiff on 2018-12-31.
 */
class ProtocolLibHooker : PluginHooker {
    lateinit var skullNbtHandler: SkullNbtHandler
    override val pluginName = "ProtocolLib"
    override var hasHooked = false

    override fun hook(server: Server) {
        skullNbtHandler = SkullNbtHandler()
        hasHooked = true
    }
}