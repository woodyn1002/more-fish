package me.elsiff.morefish.configuration

import me.elsiff.morefish.configuration.format.TextFormat
import me.elsiff.morefish.configuration.format.TextListFormat
import org.bukkit.configuration.ConfigurationSection

/**
 * Created by elsiff on 2019-01-09.
 */
abstract class ConfigurationValueAccessor {
    protected abstract val currentSection: ConfigurationSection

    val children: Set<ConfigurationSectionAccessor>
        get() = currentSection.getKeys(false).map { section(it) }.toSet()

    operator fun get(path: String): ConfigurationSectionAccessor =
        section(path)

    fun section(path: String): ConfigurationSectionAccessor {
        val handle = findValue(
            path, currentSection::getConfigurationSection,
            currentSection::isConfigurationSection, null
        )
        return ConfigurationSectionAccessor(handle)
    }

    fun contains(path: String): Boolean =
        currentSection.contains(path)

    fun format(id: String): TextFormat =
        TextFormat(string(id))

    fun formats(id: String): TextListFormat =
        TextListFormat(strings(id))

    fun text(path: String, default: String? = null): String =
        string(path, default).translated()

    fun texts(path: String, default: List<String>? = null): List<String> =
        strings(path, default).translated()

    fun boolean(path: String, default: Boolean? = null): Boolean =
        findValue(path, currentSection::getBoolean, currentSection::isBoolean, default)

    fun double(path: String, default: Double? = null): Double =
        findValue(path, currentSection::getDouble, {
            currentSection.isDouble(it) || currentSection.isInt(it)
        }, default)

    fun int(path: String, default: Int? = null): Int =
        findValue(path, currentSection::getInt, currentSection::isInt, default)

    fun long(path: String, default: Long? = null): Long =
        findValue(path, currentSection::getLong, {
            currentSection.isLong(it) || currentSection.isInt(it)
        }, default)

    fun string(path: String, default: String? = null): String =
        findValue(path, currentSection::getString, currentSection::isString, default)

    fun strings(path: String, default: List<String>? = null): List<String> =
        findValue(path, currentSection::getStringList, currentSection::isList, default)

    private inline fun <reified T> findValue(
        path: String,
        getter: (String) -> T,
        typeChecker: (String) -> Boolean,
        default: T?
    ): T {
        return if (currentSection.contains(path)) {
            require(typeChecker(path)) { "Value of '$path' in configuration is not a ${T::class.simpleName}" }
            getter(path)
        } else {
            require(default != null) { "Path '$path' must exist or have a default value" }
            default
        }
    }
}