package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class PotionEffectCondition(
    private val effectType: PotionEffectType,
    private val minAmplifier: Int
) : FishCondition {
    override fun check(caught: Item, fisher: Player, competition: FishingCompetition): Boolean {
        return fisher.hasPotionEffect(effectType) && fisher.getPotionEffect(effectType)!!.amplifier >= minAmplifier
    }
}
