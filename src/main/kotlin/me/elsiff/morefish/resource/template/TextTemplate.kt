package me.elsiff.morefish.resource.template

import me.elsiff.morefish.util.ColorUtils

/**
 * Created by elsiff on 2019-01-01.
 */
class TextTemplate(rawString: String) : Template<String> {
    private val templateString: String = ColorUtils.translate(rawString)

    override fun formatted(placeholders: Map<String, String>): String {
        var replaced = templateString
        placeholders.forEach { placeholder, replacement ->
            replaced = replaced.replace(placeholder, replacement)
        }
        return replaced
    }

    override fun formattedEmpty(): String {
        return templateString
    }

    override fun toString(): String {
        return templateString
    }

    companion object {
        fun format(rawString: String, placeholders: Map<String, String>): String {
            return TextTemplate(rawString).formatted(placeholders)
        }
    }
}