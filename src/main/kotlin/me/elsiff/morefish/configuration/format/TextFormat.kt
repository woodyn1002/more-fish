package me.elsiff.morefish.configuration.format

import me.elsiff.morefish.configuration.translated

/**
 * Created by elsiff on 2019-01-09.
 */
class TextFormat(
    private var string: String
) : Format<TextFormat, String> {
    override val output: String
        get() = string.translated()

    override fun replace(vararg pairs: Pair<String, Any>): TextFormat {
        for (pair in pairs) {
            string = string.replace(pair.first, pair.second.toString())
        }
        return this
    }
}