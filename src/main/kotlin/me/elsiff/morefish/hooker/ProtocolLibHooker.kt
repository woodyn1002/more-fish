package me.elsiff.morefish.hooker

import me.elsiff.morefish.configuration.Config
import org.bukkit.Server

/**
 * Created by elsiff on 2018-12-31.
 */
class ProtocolLibHooker : PluginHooker {
    override val pluginName = "ProtocolLib"
    override var hasHooked = false
    lateinit var skullNbtHandler: SkullNbtHandler

    override fun hook(server: Server) {
        PluginHooker.checkEnabled(this, server.pluginManager)

        skullNbtHandler = SkullNbtHandler()
        hasHooked = true
    }
}