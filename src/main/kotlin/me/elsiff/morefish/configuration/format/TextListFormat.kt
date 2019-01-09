package me.elsiff.morefish.configuration.format

import me.elsiff.morefish.configuration.translated

/**
 * Created by elsiff on 2019-01-09.
 */
class TextListFormat(
    private var strings: List<String>
) : Format<TextListFormat, List<String>> {
    override val output: List<String>
        get() = strings.translated()

    override fun replace(vararg pairs: Pair<String, Any>): TextListFormat {
        for (pair in pairs) {
            strings = strings.map { it.replace(pair.first, pair.second.toString()) }
        }
        return this
    }
}