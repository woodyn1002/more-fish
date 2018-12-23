package me.elsiff.morefish

import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    override fun onEnable() {
        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }
}