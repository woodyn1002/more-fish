package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.BroadcastEffect
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.catcheffect.CompetitionEffect
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.listener.FishingListener
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    private val resources = ResourceBundle(this)
    private val fishTypes = FishTypeTable()
    private val catchEffects = CatchEffectCollection()
    private val competition = FishingCompetition()

    override fun onEnable() {
        resources.loadAll()

        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects), this@MoreFish)
        }
        catchEffects.run {
            addEffect(BroadcastEffect())
            addEffect(CompetitionEffect(competition))
        }
        val commands = PaperCommandManager(this)
        commands.registerCommand(MainCommand(competition))

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }
}