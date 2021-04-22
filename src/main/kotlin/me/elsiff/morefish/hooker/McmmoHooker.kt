package me.elsiff.morefish.hooker

import com.gmail.nossr50.api.ExperienceAPI
import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent
import com.gmail.nossr50.events.skills.fishing.McMMOPlayerMagicHunterEvent
import me.elsiff.morefish.MoreFish
import me.elsiff.morefish.configuration.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * Created by elsiff on 2019-01-20.
 */
class McmmoHooker : PluginHooker {
    override val pluginName = "mcMMO"
    override var hasHooked: Boolean = false

    override fun hook(plugin: MoreFish) {
        Bukkit.getPluginManager().registerEvents(McmmoFishingFixer(plugin), plugin)
        hasHooked = true
    }

    fun skillLevelOf(player: Player, skillType: String): Int =
        ExperienceAPI.getLevel(player, skillType)

    class McmmoFishingFixer(val plugin: MoreFish) : Listener {

        @EventHandler(ignoreCancelled = true)
        fun onMcmmoFishOnFishContest(event: McMMOPlayerFishingTreasureEvent) = cancelMcmmoFishOnFishContest(event)

        @EventHandler(ignoreCancelled = true)
        fun onMcmmoFishOnFishContest(event: McMMOPlayerMagicHunterEvent) = cancelMcmmoFishOnFishContest(event)

        private fun cancelMcmmoFishOnFishContest(event: McMMOPlayerFishingTreasureEvent) {
            if (!Config.standard.boolean("general.only-for-contest") || plugin.competition.isEnabled()) {
                event.isCancelled = true
            }
        }
    }
}
