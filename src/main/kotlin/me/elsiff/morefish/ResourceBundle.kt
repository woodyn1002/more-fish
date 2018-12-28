package me.elsiff.morefish

import me.elsiff.morefish.configuration.FileConfigurationHandler
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

/**
 * Created by elsiff on 2018-12-28.
 */
class ResourceBundle(
        private val plugin: JavaPlugin
) {
    class Config : FileConfigurationHandler() {
        inner class General : Section(this, "general") {
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

        inner class AutoRunning : Section(this, "auto-running") {
            val enable by BooleanValue("enable")
            val requiredPlayer by IntValue("required-player")
            val timer by LongValue("timer")
            val startTime by StringListValue("start-time")
        }

        inner class FishShop : Section(this, "fish-shop") {
            val enable by BooleanValue("enable")
            val multiplier by DoubleValue("multiplier")
            val roundDecimalPoints by BooleanValue("round-decimal-points")
        }

        inner class Messages : Section(this, "messages") {
            val announceCatch by IntValue("announce-catch")
            val announceNew1st by IntValue("announce-new-1st")
            val onlyAnnounceFishingRod by BooleanValue("only-announce-fishing-rod")
            val broadcastStart by BooleanValue("broadcast-start")
            val broadcastStop by BooleanValue("broadcast-stop")
            val showTopOnEnding by BooleanValue("show-top-on-ending")
            val contestBarColor by StringValue("contest-bar-color")
            val topNumber by IntValue("top-number")
        }

        val version by IntValue("version")
        val general = General()
        val autoRunning = AutoRunning()
        val fishShop = FishShop()
        val messages = Messages()
    }
    val config = Config()

    fun loadAll() {
        config.load(plugin, Paths.get("config.yml"))
    }
}