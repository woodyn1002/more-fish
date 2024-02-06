package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import me.elsiff.morefish.util.NamespacedKeyUtils
import org.bukkit.enchantments.Enchantment

/**
 * Created by elsiff on 2019-01-09.
 */
class EnchantmentMapLoader : CustomLoader<Map<Enchantment, Int>> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Map<Enchantment, Int> {
        return if (section.contains(path)) {
            section.strings(path).associate {
                val tokens = it.split(DELIMITER)
                val enchantment = NamespacedKeyUtils.enchantment(tokens[0])
                val level = tokens[1].toInt()
                Pair(enchantment, level)
            }
        } else {
            emptyMap()
        }
    }

    companion object {
        private const val DELIMITER = '|'
    }
}
