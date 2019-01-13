package me.elsiff.morefish.fishing.catcheffect

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
interface CatchHandler {
    fun play(catcher: Player, fish: Fish)
}