package me.elsiff.morefish.resource

import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

/**
 * Created by elsiff on 2018-12-28.
 */
class ResourceBundle(
        private val plugin: JavaPlugin
) {
    val config = ConfigResource()
    val fish = FishResource()
    val lang = LangResource()

    fun loadAll() {
        config.load(plugin, Paths.get("config.yml"))
        config.general.locale.let { locale ->
            fish.load(plugin, Paths.get("locale", "fish_$locale.yml"))
            lang.load(plugin, Paths.get("locale", "lang_$locale.yml"))
        }
    }
}