package me.elsiff.morefish

import me.elsiff.morefish.fishing.FishTypeFactory
import me.elsiff.morefish.fishing.catcheffect.BroadcastEffect
import me.elsiff.morefish.fishing.catcheffect.CatchEffectFactory
import me.elsiff.morefish.listener.FishingListener
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
object MoreFish : JavaPlugin() {
    private val fishTypes = FishTypeFactory()
    private val catchEffects = CatchEffectFactory()

    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects), this@MoreFish)
        }
        catchEffects.addEffect(BroadcastEffect())
        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }
}