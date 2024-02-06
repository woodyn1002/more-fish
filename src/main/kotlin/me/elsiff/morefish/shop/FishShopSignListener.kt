package me.elsiff.morefish.shop

import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Created by elsiff on 2019-01-19.
 */
class FishShopSignListener(
    private val fishShop: FishShop
) : Listener {
    private val shopSignTitle: String
        get() = Config.standard.text("fish-shop.sign.title")

    private val shopSignCreation: String
        get() = Config.standard.string("fish-shop.sign.creation")

    @EventHandler
    fun onSignChange(event: SignChangeEvent) {
        if (event.lines[0] == shopSignCreation || event.lines[0] == shopSignTitle) {
            if (event.player.hasPermission("morefish.admin")) {
                event.setLine(0, shopSignTitle)
                event.player.sendMessage(Lang.text("created-sign-shop"))
            } else {
                event.player.sendMessage(Lang.text("no-permission"))
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK &&
            event.clickedBlock?.state is Sign
        ) {
            val sign = event.clickedBlock?.state as Sign
            if (sign.lines[0] == shopSignTitle) {
                if (Config.standard.boolean("fish-shop.enable")) {
                    fishShop.openGuiTo(event.player)
                } else {
                    event.player.sendMessage(Lang.text("shop-disabled"))
                }
            }
        }
    }
}
