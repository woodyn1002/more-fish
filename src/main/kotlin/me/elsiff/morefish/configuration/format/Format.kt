package me.elsiff.morefish.configuration.format

import me.elsiff.morefish.hooker.PlaceholderApiHooker
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2019-01-09.
 */
interface Format<T, R> {
    fun replace(vararg pairs: Pair<String, Any>): T

    fun replace(pairs: Map<String, Any>): T =
        replace(*pairs.toList().toTypedArray())

    fun output(player: Player? = null): R

    companion object {
        private lateinit var placeholderApiHooker: PlaceholderApiHooker

        fun init(placeholderApiHooker: PlaceholderApiHooker) {
            this.placeholderApiHooker = placeholderApiHooker
        }

        fun tryReplacing(string: String, player: Player?): String {
            return if (::placeholderApiHooker.isInitialized && placeholderApiHooker.hasHooked)
                placeholderApiHooker.tryReplacing(string, player)
            else
                string
        }
    }
}