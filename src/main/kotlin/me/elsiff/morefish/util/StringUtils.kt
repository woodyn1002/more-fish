package me.elsiff.morefish.util

/**
 * Created by elsiff on 2018-12-28.
 */
object StringUtils {
    fun format(string: String, placeholders: Map<String, String>): String {
        val builder = StringBuilder(string)
        placeholders.forEach { placeholder, replacement ->
            builder.replace(placeholder.toRegex(), replacement)
        }
        return ColorUtils.translate(builder.toString())
    }
}