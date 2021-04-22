package me.elsiff.morefish.configuration.format

import me.elsiff.morefish.configuration.translated
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-09.
 */
class TextFormat(
    private var string: String
) : Format<TextFormat, String> {
    override fun replace(vararg pairs: Pair<String, Any>): TextFormat {
        for (pair in pairs) {
            string = string.replace(pair.first, pair.second.toString())
        }
        return this
    }

    override fun output(player: Player?): String {
        return Format.tryReplacing(string.translated(), player)
    }
}
