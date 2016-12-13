package me.elsiff.morefish.hookers;

import me.elsiff.morefish.MoreFish;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.event.EventHandler;

@TraitName("fishshop")
public class FishShopTrait extends Trait {

    public FishShopTrait() {
        super("fishshop");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        if (event.getNPC() == this.getNPC()) {
            MoreFish.getInstance().getFishShopGUI().openGUI(event.getClicker());
        }
    }
}
