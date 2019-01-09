package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.configuration.Config
import me.elsiff.morefish.fishing.MutableFishTypeTable
import me.elsiff.morefish.fishing.catcheffect.CatchEffectCollection
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.gui.GuiOpener
import me.elsiff.morefish.gui.GuiRegistry
import me.elsiff.morefish.hooker.ProtocolLibHooker
import me.elsiff.morefish.hooker.VaultHooker
import me.elsiff.morefish.item.FishItemStackConverter
import me.elsiff.morefish.listener.FishingListener
import me.elsiff.morefish.listener.UpdateNotifierListener
import me.elsiff.morefish.shop.FishShop
import me.elsiff.morefish.util.OneTickScheduler
import me.elsiff.morefish.util.UpdateChecker
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    val protocolLib = ProtocolLibHooker()
    val vault = VaultHooker()

    val guiRegistry = GuiRegistry(this)
    val guiOpener = GuiOpener(guiRegistry)
    val oneTickScheduler = OneTickScheduler(this)
    val fishTypeTable = MutableFishTypeTable()
    val competition = FishingCompetition(this)
    val catchEffects = CatchEffectCollection(competition)
    val converter = FishItemStackConverter(this, fishTypeTable)
    val fishShop = FishShop(guiRegistry, guiOpener, oneTickScheduler, converter, vault)
    val updateChecker = UpdateChecker(22926, this.description.version)

    override fun onEnable() {
        protocolLib.hookIfEnabled(server)
        vault.hookIfEnabled(server)

        applyConfig()

        server.pluginManager.run {
            registerEvents(FishingListener(fishTypeTable, catchEffects, converter), this@MoreFish)
        }

        val commands = PaperCommandManager(this)
        val mainCommand = MainCommand(this, competition, fishShop)
        commands.registerCommand(mainCommand)

        if (!isSnapshotVersion()) {
            updateChecker.check()
            if (updateChecker.hasNewVersion()) {
                val notifier = UpdateNotifierListener(updateChecker.newVersion)
                server.pluginManager.registerEvents(notifier, this)
            }
        }

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        guiRegistry.clear(true)
        logger.info("Plugin has been disabled.")
    }

    private fun isSnapshotVersion(): Boolean {
        return this.description.version.contains("SNAPSHOT", true)
    }

    fun applyConfig() {
        Config.load(this)
        Config.customItemStackLoader.protocolLib = protocolLib

        fishTypeTable.clear()
        fishTypeTable.putAll(Config.fishTypeMapLoader.loadFrom(Config.fish))
        logger.info("Loaded ${fishTypeTable.rarities.size} rarities and ${fishTypeTable.types.size} fish types")
    }
}