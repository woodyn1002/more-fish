package me.elsiff.morefish.resource.configuration

import org.bukkit.Color
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.reflect.KProperty

/**
 * Created by elsiff on 2018-12-27.
 */
abstract class ConfigurationValueHandler {
    abstract class Root : ConfigurationValueHandler() {
        override fun pathOf(id: String): String = id
    }

    abstract class Child(
            private val parent: ConfigurationValueHandler,
            private val path: String
    ) : ConfigurationValueHandler() {
        override fun getConfiguration(): Configuration = parent.getConfiguration()

        override fun pathOf(id: String): String = "$path.$id"
    }

    abstract inner class Value<T>(
            private val id: String,
            private val getter: (Configuration, String) -> T,
            private val setter: (Configuration, String, T) -> Unit = Configuration::set
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val path = pathOf(id)
            val cfg = getConfiguration()

            check(cfg.contains(path)) { "There's no path named '$path'" }
            return getter(cfg, path)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            setter(getConfiguration(), pathOf(id), value)
        }

        fun exists(): Boolean {
            return getConfiguration().contains(pathOf(id))
        }
    }

    inner class BooleanValue(id: String) : Value<Boolean>(id, Configuration::getBoolean)

    inner class BooleanListValue(id: String) : Value<List<Boolean>>(id, Configuration::getBooleanList)

    inner class ByteListValue(id: String) : Value<List<Byte>>(id, Configuration::getByteList)

    inner class CharacterListValue(id: String) : Value<List<Char>>(id, Configuration::getCharacterList)

    inner class ColorValue(id: String) : Value<Color>(id, Configuration::getColor)

    inner class DoubleValue(id: String) : Value<Double>(id, Configuration::getDouble)

    inner class DoubleListValue(id: String) : Value<List<Double>>(id, Configuration::getDoubleList)

    inner class FloatListValue(id: String) : Value<List<Float>>(id, Configuration::getFloatList)

    inner class IntValue(id: String) : Value<Int>(id, Configuration::getInt)

    inner class IntListValue(id: String) : Value<List<Int>>(id, Configuration::getIntegerList)

    inner class ItemStackValue(id: String) : Value<ItemStack>(id, Configuration::getItemStack)

    inner class LongValue(id: String) : Value<Long>(id, Configuration::getLong)

    inner class LongListValue(id: String) : Value<List<Long>>(id, Configuration::getLongList)

    inner class OfflinePlayerkValue(id: String) : Value<OfflinePlayer>(id, Configuration::getOfflinePlayer)

    inner class ShortListValue(id: String) : Value<List<Short>>(id, Configuration::getShortList)

    inner class StringValue(id: String) : Value<String>(id, Configuration::getString)

    inner class StringListValue(id: String) : Value<List<String>>(id, Configuration::getStringList)

    inner class VectorValue(id: String) : Value<Vector>(id, Configuration::getVector)

    abstract fun pathOf(id: String): String

    fun childSections(id: String): Set<ConfigurationSection> {
        val section = getConfiguration().getConfigurationSection(pathOf(id))
        return section.getKeys(false).map {
            section.getConfigurationSection(it)
        }.toSet()
    }

    protected abstract fun getConfiguration(): Configuration

    fun ConfigurationSection.childSections(): Set<ConfigurationSection> {
        return getKeys(false).map { getConfigurationSection(it) }.toSet()
    }
}