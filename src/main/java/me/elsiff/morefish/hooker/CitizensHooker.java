package me.elsiff.morefish.hooker;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class CitizensHooker {
    private TraitInfo fishShop;

    public CitizensHooker() {
        this.fishShop = TraitInfo.create(FishShopTrait.class).withName("fishshop");
    }

    public void registerTrait() {
        CitizensAPI.getTraitFactory().registerTrait(fishShop);
    }

    public void deregisterTrait() {
        CitizensAPI.getTraitFactory().deregisterTrait(fishShop);
    }
}
