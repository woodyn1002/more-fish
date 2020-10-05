package me.elsiff.morefish.hooker

import me.elsiff.morefish.MoreFish
import net.milkbowl.vault.economy.Economy

/**
 * Created by elsiff on 2019-01-04.
 */
class VaultHooker : PluginHooker {
    override val pluginName = "Vault"
    override var hasHooked = false
    lateinit var economy: Economy

    override fun hook(plugin: MoreFish) {
        PluginHooker.checkEnabled(this, plugin.server.pluginManager)

        val registration = plugin.server.servicesManager.getRegistration(Economy::class.java)
        if (registration != null) {
            economy = registration.provider
        }
        hasHooked = true
    }

    fun hasEconomy(): Boolean {
        return ::economy.isInitialized
    }
}