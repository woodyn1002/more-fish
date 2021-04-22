package me.elsiff.morefish.hooker

import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.shop.FishShopKeeperTrait
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo

/**
 * Created by elsiff on 2019-01-24.
 */
class CitizensHooker : PluginHooker {
    override val pluginName = "Citizens"
    override var hasHooked = false
    private lateinit var traitInfo: TraitInfo

    override fun hook(plugin: MoreFish) {
        traitInfo = TraitInfo.create(FishShopKeeperTrait::class.java)
        CitizensAPI.getTraitFactory().registerTrait(traitInfo)
        FishShopKeeperTrait.init(plugin.fishShop)
        hasHooked = true
    }

    fun dispose() {
        CitizensAPI.getTraitFactory().deregisterTrait(traitInfo)
    }
}
