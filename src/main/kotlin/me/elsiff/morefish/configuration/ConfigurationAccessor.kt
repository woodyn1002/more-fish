package me.elsiff.morefish.configuration

import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.nio.file.Path

/**
 * Created by elsiff on 2019-01-09.
 */
class ConfigurationAccessor : ConfigurationValueAccessor() {
    private lateinit var configuration: Configuration
    private lateinit var _filePath: Path

    val filePath: Path
        get() = _filePath

    override val currentSection: ConfigurationSection
        get() {
            check(::configuration.isInitialized) { "Configuration hasn't been loaded yet" }
            return configuration
        }

    fun loadFromYaml(plugin: Plugin, resourcePath: Path) {
        _filePath = resourcePath
        val dataPath = plugin.dataFolder.toPath()
        val configurationFile = dataPath.resolve(resourcePath).toFile()

        if (!configurationFile.exists()) {
            plugin.saveResource(resourcePath.toString(), false)
        }

        configuration = YamlConfiguration().apply { load(configurationFile) }
    }
}
