package me.elsiff.morefish

import net.milkbowl.vault.economy.Economy
import org.bukkit.Server

/**
 * Created by elsiff on 2019-01-04.
 */
class VaultHooker : PluginHooker {
    override val pluginName = "Vault"
    override var hasHooked = false
    lateinit var economy: Economy

    override fun hook(server: Server) {
        val registration = server.servicesManager.getRegistration(Economy::class.java) ?: null
        if (registration != null) {
            economy = registration.provider
        }
        hasHooked = true
    }

    fun hasEconomy(): Boolean {
        return ::economy.isInitialized
    }
}