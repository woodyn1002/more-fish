package me.elsiff.morefish.shop

import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.configuration.Lang
import net.citizensnpcs.api.event.NPCRightClickEvent
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import org.bukkit.event.EventHandler

/**
 * Created by elsiff on 2019-01-24.
 */
@TraitName("fishshop")
class FishShopKeeperTrait : Trait("fishshop") {
    @EventHandler
    fun onClickNpc(event: NPCRightClickEvent) {
        if (event.npc == this.npc && MoreFish.instance.isEnabled) {
            if (Config.standard.boolean("fish-shop.enable")) {
                fishShop.openGuiTo(event.clicker)
            } else {
                event.clicker.sendMessage(Lang.text("shop-disabled"))
            }
        }
    }

    companion object {
        private lateinit var fishShop: FishShop

        fun init(fishShop: FishShop) {
            this.fishShop = fishShop
        }
    }
}