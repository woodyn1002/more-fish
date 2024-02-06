package me.elsiff.morefish.fishing.catchhandler

import me.elsiff.morefish.fishing.Fish
import org.bukkit.entity.Player

/**
 * Created by elsiff on 2018-12-25.
 */
interface CatchHandler {
    fun handle(catcher: Player, fish: Fish)
}
