package me.elsiff.morefish.hooker

import com.gmail.nossr50.api.ExperienceAPI
import org.bukkit.Server
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-20.
 */
class McmmoHooker : PluginHooker {
    override val pluginName = "mcMMO"
    override var hasHooked: Boolean = false

    override fun hook(server: Server) {
        hasHooked = true
    }

    fun skillLevelOf(player: Player, skillType: String): Int =
        ExperienceAPI.getLevel(player, skillType)
}