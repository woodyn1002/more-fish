package me.elsiff.morefish.configuration

import org.bukkit.configuration.ConfigurationSection

/**
 * Created by elsiff on 2019-01-09.
 */
class ConfigurationSectionAccessor(
    override val currentSection: ConfigurationSection
) : ConfigurationValueAccessor() {
    val name: String
        get() = currentSection.name
}