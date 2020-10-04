package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.fishing.condition.*
import me.elsiff.morefish.hooker.McmmoHooker
import me.elsiff.morefish.hooker.WorldGuardHooker
import me.elsiff.morefish.util.NamespacedKeyUtils
import org.apache.commons.lang.math.DoubleRange
import org.bukkit.NamespacedKey
import org.bukkit.block.Biome
import org.bukkit.enchantments.Enchantment
import java.lang.IllegalArgumentException

/**
 * Created by elsiff on 2019-01-09.
 */
class FishConditionSetLoader : CustomLoader<Set<FishCondition>> {
    private lateinit var mcmmoHooker: McmmoHooker
    private lateinit var worldGuardHooker: WorldGuardHooker

    fun init(mcmmoHooker: McmmoHooker, worldGuardHooker: WorldGuardHooker) {
        this.mcmmoHooker = mcmmoHooker
        this.worldGuardHooker = worldGuardHooker
    }

    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Set<FishCondition> {
        return if (section.contains(path)) {
            section.strings(path).map {
                it.split(DELIMITER).let { tokens ->
                    fishConditionFrom(
                        id = tokens[0],
                        args = tokens.subList(1, tokens.size)
                    )
                }
            }.toSet()
        } else {
            emptySet()
        }
    }

    private fun fishConditionFrom(id: String, args: List<String>): FishCondition {
        return when (id) {
            "raining" ->
                RainingCondition(args[0].toBoolean())
            "thundering" ->
                ThunderingCondition(args[0].toBoolean())
            "time" ->
                TimeCondition(TimeCondition.TimeState.valueOf(args[0].toUpperCase()))
            "biome" ->
                BiomeCondition(args.map { Biome.valueOf(it.toUpperCase()) })
            "enchantment" ->
                EnchantmentCondition(
                    NamespacedKeyUtils.enchantment(args[0])
                    args[1].toInt()
                )
            "level" ->
                XpLevelCondition(args[0].toInt())
            "contest" ->
                CompetitionCondition(FishingCompetition.State.valueOf(args[0].toUpperCase()))
            "potion-effect" ->
                PotionEffectCondition(NamespacedKeyUtils.potionEffectType(args[0]), args[1].toInt())
            "location-y" ->
                LocationYCondition(DoubleRange(args[0].toDouble(), args[1].toDouble()))
            "mcmmo-skill" ->
                McmmoSkillCondition(mcmmoHooker, args[0], args[1].toInt())
            "worldguard-region" ->
                WorldGuardRegionCondition(worldGuardHooker, args[0])
            else ->
                throw IllegalStateException("There's no fish condition whose id is '$id'")
        }
    }

    companion object {
        private const val DELIMITER = '|'
    }
}