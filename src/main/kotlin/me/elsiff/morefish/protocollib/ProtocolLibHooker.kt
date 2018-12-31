package me.elsiff.morefish.protocollib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import me.elsiff.morefish.PluginHooker
import me.elsiff.morefish.fishing.FishTypeTable
import org.bukkit.plugin.PluginManager

/**
 * Created by elsiff on 2018-12-31.
 */
class ProtocolLibHooker(
        private val fishTypes: FishTypeTable
) : PluginHooker {
    lateinit var skullNbtHandler: SkullNbtHandler
    lateinit var fishNbtHandler: FishNbtHandler
    override val pluginName = "ProtocolLib"
    override var hasHooked = false
    private lateinit var protocolManager: ProtocolManager

    override fun hook(pluginManager: PluginManager) {
        protocolManager = ProtocolLibrary.getProtocolManager()
        skullNbtHandler = SkullNbtHandler()
        fishNbtHandler = FishNbtHandler(fishTypes)
        hasHooked = true
    }
}