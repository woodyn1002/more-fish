package me.elsiff.morefish.util

/**
 * Created by elsiff on 2018-12-28.
 */
object StringUtils {
    fun format(string: String, placeholders: Map<String, String>): String {
        var replaced = string
        placeholders.forEach { placeholder, replacement ->
            replaced = replaced.replace(placeholder, replacement)
        }
        return ColorUtils.translate(replaced)
    }
}