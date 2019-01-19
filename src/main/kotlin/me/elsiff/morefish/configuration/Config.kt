package me.elsiff.morefish.configuration

import me.elsiff.morefish.announcement.PlayerAnnouncement
import me.elsiff.morefish.configuration.loader.*
import org.bukkit.plugin.Plugin
import java.nio.file.Paths

/**
 * Created by elsiff on 2019-01-09.
 */
object Config {
    val standard: ConfigurationAccessor = ConfigurationAccessor()
    val fish: ConfigurationAccessor = ConfigurationAccessor()
    val lang: ConfigurationAccessor = ConfigurationAccessor()

    val chatColorLoader: ChatColorLoader = ChatColorLoader()
    val enchantmentMapLoader: EnchantmentMapLoader = EnchantmentMapLoader()
    val customItemStackLoader: CustomItemStackLoader = CustomItemStackLoader(enchantmentMapLoader)
    val playerAnnouncementLoader: PlayerAnnouncementLoader = PlayerAnnouncementLoader()
    val fishRaritySetLoader: FishRaritySetLoader = FishRaritySetLoader(chatColorLoader, playerAnnouncementLoader)
    val fishConditionSetLoader: FishConditionSetLoader = FishConditionSetLoader()
    val fishTypeMapLoader: FishTypeMapLoader =
        FishTypeMapLoader(fishRaritySetLoader, customItemStackLoader, fishConditionSetLoader, playerAnnouncementLoader)
    val localTimeListLoader: LocalTimeListLoader = LocalTimeListLoader()

    val defaultCatchAnnouncement: PlayerAnnouncement
        get() = playerAnnouncementLoader.loadFrom(standard["messages"], "announce-catch")
    val newFirstAnnouncement: PlayerAnnouncement
        get() = playerAnnouncementLoader.loadFrom(standard["messages"], "announce-new-1st")

    private val configurationVersionMap: Map<ConfigurationAccessor, Int> = mapOf(
        standard to 220,
        fish to 220,
        lang to 211
    )

    fun load(plugin: Plugin) {
        val configPath = Paths.get("config.yml")
        val localePath = Paths.get("locale")

        standard.loadFromYaml(plugin, configPath)
        standard.string("general.locale").let { locale ->
            fish.loadFromYaml(plugin, localePath.resolve("fish_$locale.yml"))
            lang.loadFromYaml(plugin, localePath.resolve("lang_$locale.yml"))
        }

        for ((cfg, requiredVersion) in configurationVersionMap) {
            if (cfg.int("version") < requiredVersion) {
                val msg = Lang.format("old-file")
                    .replace("%s" to cfg.filePath.fileName)
                    .output
                plugin.server.consoleSender.sendMessage(msg)
            }
        }
    }
}