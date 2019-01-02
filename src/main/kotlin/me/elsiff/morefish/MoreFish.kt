package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.item.FishItemStackConverter
import me.elsiff.morefish.listener.FishingListener
import me.elsiff.morefish.resource.ResourceProvider
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    private val resourceProvider = ResourceProvider(this)
    val fishTypes = FishTypeTable()
    val competition = FishingCompetition(this)
    val catchEffects = CatchEffectCollection(competition)
    val converter = FishItemStackConverter(fishTypes)

    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects, converter), this@MoreFish)
        }

        val commands = PaperCommandManager(this)
        val mainCommand = MainCommand(description, competition, resourceProvider)
        commands.registerCommand(mainCommand)

        resourceProvider.run {
            addReceiver(fishTypes)
            addReceiver(competition)
            addReceiver(catchEffects)
            addReceiver(converter)
            addReceiver(mainCommand)
            provideAll()
        }
        logger.info("Loaded ${fishTypes.rarities().size} rarities and ${fishTypes.types().size} fish types")

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }
}