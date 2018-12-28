package me.elsiff.morefish

import co.aikar.commands.PaperCommandManager
import me.elsiff.morefish.command.MainCommand
import me.elsiff.morefish.configuration.FileConfigurationHandler
import me.elsiff.morefish.fishing.FishTypeFactory
import me.elsiff.morefish.fishing.catcheffect.BroadcastEffect
import me.elsiff.morefish.fishing.catcheffect.CatchEffectFactory
import me.elsiff.morefish.fishing.catcheffect.CompetitionEffect
import me.elsiff.morefish.fishing.competition.FishingCompetition
import me.elsiff.morefish.listener.FishingListener
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

/**
 * Created by elsiff on 2018-12-20.
 */
class MoreFish : JavaPlugin() {
    private val config = object : FileConfigurationHandler() {
        val version by IntValue("version")
        val general = object : Section(this, "general") {
            val locale by StringValue("locale")
            val checkUpdate by BooleanValue("check-update")
            val autoStart by BooleanValue("auto-start")
            val useBossBar by BooleanValue("use-boss-bar")
            val onlyForContest by BooleanValue("only-for-contest")
            val noFishingUnlessContest by BooleanValue("no-fishing-unless-contest")
            val contestDisabledWorlds by StringListValue("contest-disabled-worlds")
            val replaceOnlyFish by BooleanValue("replace-only-fish")
            val saveRecords by BooleanValue("save-records")
        }
        val autoRunning = object : Section(this, "auto-running") {
            val enable by BooleanValue("enable")
            val requiredPlayer by IntValue("required-player")
            val timer by LongValue("timer")
            val startTime by StringListValue("start-time")
        }
        val fishShop = object : Section(this, "fish-shop") {
            val enable by BooleanValue("enable")
            val multiplier by DoubleValue("multiplier")
            val roundDecimalPoints by BooleanValue("round-decimal-points")
        }
        val messages = object : Section(this, "messages") {
            val announceCatch by IntValue("announce-catch")
            val announceNew1st by IntValue("announce-new-1st")
            val onlyAnnounceFishingRod by BooleanValue("only-announce-fishing-rod")
            val broadcastStart by BooleanValue("broadcast-start")
            val broadcastStop by BooleanValue("broadcast-stop")
            val showTopOnEnding by BooleanValue("show-top-on-ending")
            val contestBarColor by StringValue("contest-bar-color")
            val topNumber by IntValue("top-number")
        }
    }
    private val fishTypes = FishTypeFactory()
    private val catchEffects = CatchEffectFactory()
    private val competition = FishingCompetition()

    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(FishingListener(fishTypes, catchEffects), this@MoreFish)
        }
        catchEffects.run {
            addEffect(BroadcastEffect())
            addEffect(CompetitionEffect(competition))
        }
        val commands = PaperCommandManager(this)
        commands.registerCommand(MainCommand(competition))

        config.load(this, Paths.get("config.yml"))

        logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        logger.info("Plugin has been disabled.")
    }
}