package me.elsiff.morefish.resource.template

/**
 * Created by elsiff on 2019-01-01.
 */
class TextListTemplate(rawStrings: List<String>) : Template<List<String>> {
    private val templateStrings = rawStrings.map { TextTemplate(it) }

    companion object {
        fun format(rawStrings: List<String>, placeholders: Map<String, String>): List<String> {
            return TextListTemplate(rawStrings).formatted(placeholders)
        }
    }

    override fun formatted(placeholders: Map<String, String>): List<String> {
        return templateStrings.map { it.formatted(placeholders) }
    }

    override fun formattedEmpty(): List<String> {
        return templateStrings.map { it.formattedEmpty() }
    }

    override fun toString(): String {
        return templateStrings.toString()
    }
}