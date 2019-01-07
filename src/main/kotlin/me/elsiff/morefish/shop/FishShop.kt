package me.elsiff.morefish.shop

import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.gui.GuiOpener
import me.elsiff.morefish.gui.GuiRegistry
import me.elsiff.morefish.item.FishItemStackConverter
import me.elsiff.morefish.resource.ResourceBundle
import me.elsiff.morefish.resource.ResourceReceiver
import me.elsiff.morefish.resource.template.TemplateBundle
import me.elsiff.morefish.util.OneTickScheduler
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import kotlin.math.floor

/**
 * Created by elsiff on 2019-01-03.
 */
class FishShop(
    private val guiRegistry: GuiRegistry,
    private val guiOpener: GuiOpener,
    private val oneTickScheduler: OneTickScheduler,
    private val converter: FishItemStackConverter
) : ResourceReceiver {
    var enabled = false
    private var priceMultiplier = 0.0
    private var roundDecimalPoints = false
    private var economy: Economy? = null
    private lateinit var templates: TemplateBundle

    override fun receiveResource(resources: ResourceBundle) {
        for (viewer in guiRegistry.guis.filter { it is FishShopGui }.map { it.viewers }.flatten()) {
            viewer.closeInventory()
        }
        if (resources.config.getBoolean("fish-shop.enable")) {
            check(resources.vault.hasHooked) { "Vault must be hooked for fish shop feature" }
            check(resources.vault.hasEconomy()) { "Vault doesn't have economy plugin" }
            check(resources.vault.economy.isEnabled) { "Economy must be enabled" }

            priceMultiplier = resources.config.getDouble("fish-shop.multiplier")
            roundDecimalPoints = resources.config.getBoolean("fish-shop.round-decimal-points")
            economy = resources.vault.economy
            templates = resources.templates
            enabled = true
        }
    }

    fun sell(player: Player, fish: Fish) {
        val economy = this.economy!!
        val price = priceOf(fish)
        economy.depositPlayer(player, price)
    }

    fun priceOf(fish: Fish): Double {
        val rarityPrice = fish.type.rarity.feature.additionalPrice
        val price = (priceMultiplier * fish.length) + rarityPrice

        return if (roundDecimalPoints) {
            floor(price)
        } else {
            price
        }
    }

    fun openGuiTo(player: Player) {
        val gui = FishShopGui(this, converter, oneTickScheduler, templates, player)
        guiOpener.open(player, gui)
    }
}