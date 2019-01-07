package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.gui.GuiOpener
import me.elsiff.morefish.gui.GuiRegistry
import me.elsiff.morefish.item.FishItemStackConverter
import me.elsiff.morefish.listener.FishingListener
import me.elsiff.morefish.listener.UpdateNotifierListener
import me.elsiff.morefish.resource.ResourceProvider
import me.elsiff.morefish.shop.FishShop
import me.elsiff.morefish.util.OneTickScheduler
import me.elsiff.morefish.util.UpdateChecker
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    private val resourceProvider = ResourceProvider(this)
    val guiRegistry = GuiRegistry(this)
    val guiOpener = GuiOpener(guiRegistry)
    val oneTickScheduler = OneTickScheduler(this)
    val fishTypes = FishTypeTable()
    val competition = FishingCompetition(this)
    val catchEffects = CatchEffectCollection(competition)
    val converter = FishItemStackConverter(this, fishTypes)
    val fishShop = FishShop(guiRegistry, guiOpener, oneTickScheduler, converter)
    val updateChecker = UpdateChecker(22926, this.description.version)

    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects, converter), this@MoreFish)
        }

        val commands = PaperCommandManager(this)
        val mainCommand = MainCommand(description, competition, resourceProvider, fishShop)
        commands.registerCommand(mainCommand)

        if (!isSnapshotVersion()) {
            updateChecker.check()
            if (updateChecker.hasNewVersion()) {
                val notifier = UpdateNotifierListener(updateChecker.newVersion)
                server.pluginManager.registerEvents(notifier, this)
                resourceProvider.addReceiver(notifier)
            }
        }

        resourceProvider.run {
            addReceiver(fishTypes)
            addReceiver(competition)
            addReceiver(catchEffects)
            addReceiver(converter)
            addReceiver(fishShop)
            addReceiver(mainCommand)
            provideAll()
        }
        logger.info("Loaded ${fishTypes.rarities().size} rarities and ${fishTypes.types().size} fish types")

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        guiRegistry.clear(true)
        logger.info("Plugin has been disabled.")
    }

    private fun isSnapshotVersion(): Boolean {
        return this.description.version.contains("SNAPSHOT", true)
    }
}