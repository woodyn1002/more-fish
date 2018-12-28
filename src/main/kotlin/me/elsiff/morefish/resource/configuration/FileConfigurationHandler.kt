package me.elsiff.morefish.resource.configuration

import org.bukkit.configuration.Configuration
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Path

/**
 * Created by elsiff on 2018-12-26.
 */
abstract class FileConfigurationHandler : ConfigurationValueHandler.Root() {
    private lateinit var configuration: FileConfiguration
    private lateinit var configurationFile: File

    override fun getConfiguration(): Configuration {
        check(::configuration.isInitialized) { "Configuration hasn't been loaded yet" }
        return configuration
    }

    fun load(plugin: JavaPlugin, filePath: Path) {
        configurationFile = plugin.dataFolder.toPath().resolve(filePath).toFile()

        if (!configurationFile.exists()) {
            plugin.saveResource(filePath.toString(), false)
        }
        configuration = YamlConfiguration().apply { load(configurationFile) }
    }

    fun saveChanges() {
        check(::configuration.isInitialized) { "Configuration hasn't been loaded yet" }
        configuration.save(configurationFile)
    }

    abstract inner class Section(
            parent: ConfigurationValueHandler,
            id: String
    ) : Child(parent, parent.pathOf(id))
}