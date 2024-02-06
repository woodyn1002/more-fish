package me.elsiff.morefish.fishing.condition

import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.hooker.McmmoHooker
import me.elsiff.morefish.hooker.PluginHooker
import org.bukkit.entity.Item
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-12.
 */
class McmmoSkillCondition(
    private val mcmmoHooker: McmmoHooker,
    private val skillType: String,
    private val minLevel: Int
) : FishCondition {
    override fun check(caught: Item, fisher: Player, competition: FishingCompetition): Boolean {
        PluginHooker.checkEnabled(mcmmoHooker, fisher.server.pluginManager)
        return mcmmoHooker.skillLevelOf(fisher, skillType) >= minLevel
    }
}
