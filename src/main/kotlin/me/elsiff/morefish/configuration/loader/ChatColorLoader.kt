package me.elsiff.morefish.configuration.loader

import me.elsiff.morefish.configuration.ConfigurationValueAccessor
import org.bukkit.ChatColor

/**
 * Created by elsiff on 2019-01-09.
 */
class ChatColorLoader : CustomLoader<ChatColor> {
    override fun loadFrom(section: ConfigurationValueAccessor, path: String): ChatColor {
        val colorName = section.string(path).toUpperCase()
        return ChatColor.valueOf(colorName)
    }
}
