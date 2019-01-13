package me.elsiff.morefish.configuration

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
    val fishRaritySetLoader: FishRaritySetLoader = FishRaritySetLoader(chatColorLoader)
    val fishConditionSetLoader: FishConditionSetLoader = FishConditionSetLoader()
    val fishTypeMapLoader: FishTypeMapLoader =
        FishTypeMapLoader(fishRaritySetLoader, customItemStackLoader, fishConditionSetLoader)

    fun load(plugin: Plugin) {
        val configPath = Paths.get("config.yml")
        val localePath = Paths.get("locale")

        standard.loadFromYaml(plugin, configPath)
        standard.string("general.locale").let { locale ->
            fish.loadFromYaml(plugin, localePath.resolve("fish_$locale.yml"))
            lang.loadFromYaml(plugin, localePath.resolve("lang_$locale.yml"))
        }
    }
}