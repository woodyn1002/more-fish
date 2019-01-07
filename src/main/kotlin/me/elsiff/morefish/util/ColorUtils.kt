package me.elsiff.morefish.util

import org.bukkit.ChatColor

/**
 * Created by elsiff on 2018-12-28.
 */
object ColorUtils {
    private const val ALTERNATE_COLOR_CODE = '&'

    fun translate(string: String): String {
        return ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CODE, string)
    }
}