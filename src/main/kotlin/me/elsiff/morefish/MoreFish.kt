package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.BroadcastEffect
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.catcheffect.CompetitionEffect
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.item.FishItemStackConverter
import me.elsiff.morefish.listener.FishingListener
import me.elsiff.morefish.protocollib.ProtocolLibHooker
import me.elsiff.morefish.resource.ResourceBundle
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    val resources = ResourceBundle(this)
    val fishTypes = FishTypeTable()
    val catchEffects = CatchEffectCollection()
    val competition = FishingCompetition()
    val protocolLib = ProtocolLibHooker(fishTypes)
    val converter = FishItemStackConverter(resources, protocolLib)

    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects, converter), this@MoreFish)
        }

        val commands = PaperCommandManager(this)
        commands.registerCommand(MainCommand(this))

        loadAndApplyResources()

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }

    fun loadAndApplyResources() {
        resources.loadAll()
        catchEffects.run {
            clear()
            addEffect(BroadcastEffect())
            addEffect(CompetitionEffect(competition))
        }
        protocolLib.hookIfEnabled(server.pluginManager)
        fishTypes.load(resources.fish, protocolLib)
        logger.info("Loaded ${fishTypes.rarities().size} rarities and ${fishTypes.types().size} fish types")
    }
}