package me.elsiff.morefish.dao.yaml

import me.elsiff.morefish.dao.RecordDao
import me.elsiff.morefish.fishing.Fish
import me.elsiff.morefish.fishing.FishTypeTable
import me.elsiff.morefish.fishing.competition.Record
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.*
import kotlin.math.min

/**
 * Created by elsiff on 2019-01-18.
 */
class YamlRecordDao(
    private val plugin: Plugin,
    private val fishTypeTable: FishTypeTable
) : RecordDao {
    private val yaml: YamlConfiguration
    private val file: File

    init {
        val path = plugin.dataFolder.toPath().resolve("records")
        file = path.toFile()
        if (!file.exists()) {
            file.createNewFile()
        }
        yaml = YamlConfiguration.loadConfiguration(file)
    }

    override fun insert(record: Record) {
        val id = record.fisher.uniqueId.toString()
        require(!yaml.contains(id)) { "Record must not exist in the ranking" }

        setRecord(yaml.createSection(id), record)
        yaml.save(file)
    }

    override fun update(record: Record) {
        val id = record.fisher.uniqueId.toString()
        require(yaml.isConfigurationSection(id)) { "Record must exist in the ranking" }
        // isConfigurationSection checks nullability.
        setRecord(yaml.getConfigurationSection(id)!!, record)
        yaml.save(file)
    }

    private fun setRecord(section: ConfigurationSection, record: Record) {
        section.set("fish-type", record.fish.type.name)
        section.set("fish-length", record.fish.length)
    }

    override fun clear() {
        for (key in yaml.getKeys(false)) {
            yaml.set(key, null)
        }
        yaml.save(file)
    }

    override fun top(size: Int): List<Record> {
        val list = all()
        return list.subList(0, min(5, list.size))
    }

    override fun all(): List<Record> {
        val records = mutableListOf<Record>()
        yaml.getKeys(false).filter(yaml::isConfigurationSection).map(yaml::getConfigurationSection).forEach { section ->
            val id = UUID.fromString(section!!.name)
            val player = plugin.server.getOfflinePlayer(id)

            val fishTypeName = section.getString("fish-type")
            val fishType = fishTypeTable.types.find { it.name == fishTypeName }
                    ?: throw IllegalStateException("Fish type doesn't exist for '$fishTypeName'")
            val fishLength = section.getDouble("fish-length")

            val fish = Fish(fishType, fishLength)
            records.add(Record(player, fish))
        }
        return records.sortedDescending()
    }
}
