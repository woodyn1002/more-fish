package me.elsiff.morefish.resource

import me.elsiff.morefish.resource.template.TemplateBundle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by elsiff on 2018-12-28.
 */
class ResourceProvider(
        private val plugin: JavaPlugin
) {
    private val resources = ResourceBundle()
    private val configurationFiles = mutableMapOf<FileConfiguration, File>()
    private val dataPath = plugin.dataFolder.toPath()
    private val resourceDependents = mutableSetOf<ResourceReceiver>()

    fun addReceiver(resourceDependent: ResourceReceiver) {
        resourceDependents.add(resourceDependent)
    }

    fun provideAll() {
        readyResourceBundle()
        resourceDependents.forEach {
            it.receiveResource(resources)
        }
    }

    private fun readyResourceBundle() {
        resources.apply {
            val configPath = Paths.get("config.yml")
            val localePath = Paths.get("locale")

            config = loadYamlConfiguration(configPath)
            config.getString("general.locale").let { locale ->
                fish = loadYamlConfiguration(localePath.resolve("fish_$locale.yml"))
                lang = loadYamlConfiguration(localePath.resolve("lang_$locale.yml"))
            }
            templates = TemplateBundle(lang)

            protocolLib.hookIfEnabled(plugin.server.pluginManager)
        }
    }

    private fun loadYamlConfiguration(resourcePath: Path): YamlConfiguration {
        val configurationFile = dataPath.resolve(resourcePath).toFile()
        if (!configurationFile.exists()) {
            plugin.saveResource(resourcePath.toString(), false)
        }

        val configuration = YamlConfiguration().apply { load(configurationFile) }
        configurationFiles[configuration] = configurationFile
        return configuration
    }
}