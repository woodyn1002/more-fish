package me.elsiff.morefish.condition;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectCondition implements Condition {
    private PotionEffectType effectType;
    private int amplifier;

    public PotionEffectCondition(PotionEffectType effectType, int amplifier) {
        this.effectType = effectType;
        this.amplifier = amplifier;
    }

    @Override
    public boolean isSatisfying(Player player) {
        return (player.hasPotionEffect(effectType) &&
                player.getPotionEffect(effectType).getAmplifier() >= amplifier);
    }
}
