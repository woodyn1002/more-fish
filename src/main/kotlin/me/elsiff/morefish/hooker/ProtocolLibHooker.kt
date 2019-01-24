package me.elsiff.morefish.hooker

import me.elsiff.morefish.MoreFish

/**
 * Created by elsiff on 2018-12-31.
 */
class ProtocolLibHooker : PluginHooker {
    override val pluginName = "ProtocolLib"
    override var hasHooked = false
    lateinit var skullNbtHandler: SkullNbtHandler

    override fun hook(plugin: MoreFish) {
        PluginHooker.checkEnabled(this, plugin.server.pluginManager)

        skullNbtHandler = SkullNbtHandler()
        hasHooked = true
    }
}