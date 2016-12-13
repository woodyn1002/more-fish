package me.elsiff.morefish.hookers;

import me.elsiff.morefish.MoreFish;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class CitizensHooker {
    private final MoreFish plugin;
    private TraitInfo fishShop;

    public CitizensHooker(MoreFish plugin) {
        this.plugin = plugin;
        this.fishShop = TraitInfo.create(FishShopTrait.class).withName("fishshop");
    }

    public void registerTrait() {
        CitizensAPI.getTraitFactory().registerTrait(fishShop);
    }

    public void deregisterTrait() {
        CitizensAPI.getTraitFactory().deregisterTrait(fishShop);
    }
}
