package me.elsiff.morefish.configuration

import org.bukkit.ChatColor

/**
 * Created by elsiff on 2019-01-09.
 */
internal fun String.translated(): String =
    ChatColor.translateAlternateColorCodes(Lang.ALTERNATE_COLOR_CODE, this)

internal fun List<String>.translated(): List<String> =
    this.map(String::translated)
