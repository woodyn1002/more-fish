package me.elsiff.morefish.hooker

import com.gmail.nossr50.api.ExperienceAPI
import com.gmail.nossr50.datatypes.skills.PrimarySkillType
import me.elsiff.morefish.MoreFish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-20.
 */
class McmmoHooker : PluginHooker {
    override val pluginName = "mcMMO"
    override var hasHooked: Boolean = false

    override fun hook(plugin: MoreFish) {
        hasHooked = true
    }

    fun skillLevelOf(player: Player, skillType: String): Int {
        val skill = PrimarySkillType.getSkill(skillType) ?: return 0
        return ExperienceAPI.getLevel(player, skill)
    }
}
