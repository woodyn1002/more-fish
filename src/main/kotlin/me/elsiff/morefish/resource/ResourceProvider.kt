package me.elsiff.morefish.resource

import me.elsiff.morefish.resource.template.TemplateBundle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Path

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
            val configPath = dataPath.resolve("config.yml")
            val localePath = dataPath.resolve("locale")

            config = loadYamlConfiguration(configPath)
            config.getString("general.locale").let { locale ->
                fish = loadYamlConfiguration(localePath.resolve("fish_$locale.yml"))
                lang = loadYamlConfiguration(localePath.resolve("lang_$locale.yml"))
            }
            templates = TemplateBundle(lang)

            protocolLib.hookIfEnabled(plugin.server.pluginManager)
        }
    }

    private fun loadYamlConfiguration(filePath: Path): YamlConfiguration {
        val configurationFile = filePath.toFile()
        if (!configurationFile.exists()) {
            plugin.saveResource(filePath.toString(), false)
        }

        val configuration = YamlConfiguration().apply { load(configurationFile) }
        configurationFiles[configuration] = configurationFile
        return configuration
    }
}